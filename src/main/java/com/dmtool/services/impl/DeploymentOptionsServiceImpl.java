package com.dmtool.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.DeloymentOptionsDao;
import com.dmtool.domain.AdmConfig;
import com.dmtool.domain.CommandParams;
import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.DeploymentOptions;
import com.dmtool.domain.EnvInfo;
import com.dmtool.domain.Repos;
import com.dmtool.services.AdmConfigService;
import com.dmtool.services.CommandParamsService;
import com.dmtool.services.CommandTemplatesService;
import com.dmtool.services.DeploymentOptionsService;
import com.dmtool.services.EnvService;
import com.dmtool.services.ReposService;
import com.dmtool.user.actions.DeploymentOptionsActions;
import com.dmtool.utils.DMCommandTokens;

@Repository
public class DeploymentOptionsServiceImpl implements DeploymentOptionsService {
	private static final Logger logger = Logger
			.getLogger(DeploymentOptionsServiceImpl.class);

	@Autowired
	private DeloymentOptionsDao deloymentOptionsDao;
	
	@Autowired
	private CommandTemplatesService compTemplService;
	
	@Autowired
	private EnvService envService;

	@Autowired
	private ReposService reposService;

	@Autowired
	private AdmConfigService admConfigService;

	@Autowired
	private CommandTemplatesService commandTemplatesService;

	@Autowired
	private CommandParamsService commandParamsService;

	@Override
	public HashMap<String, HashMap<String, List<String>>> processdeDloymentOptionsService(DeploymentOptionsActions env_p) {
		logger.info("Executing processdeDloymentOptionsService method:"
				+ env_p.getDeploymentServices());
		HashMap<String, HashMap<String, List<String>>> resultAndErrorMap = new HashMap<String, HashMap<String, List<String>>>();
		StringBuilder sb = new StringBuilder();
		try {
			List<String> services = env_p.getDeploymentServices();
			String envId = null;
			Integer envIdInt = env_p.getId();
			if(envIdInt != null){
				envId = envIdInt.toString();//env_p.getEnvName();
			}
			envId = env_p.getEnvName();
			String actionType = env_p.getActionType();

			HashMap<String, List<String>> errorMap = new HashMap<String, List<String>>();
			HashMap<String, List<String>> resultMap = new HashMap<String, List<String>>();

			if (envId == null || services == null || !(services.size() > 0)) {
				logger.error("No actions selected.");
				List<String> errors = new ArrayList<String>();
				errors.add("Environment or Actions not selected. Please select the Environment and Action or Command.");
				errorMap.put("Errors", errors);
			} else {
				for (int i = 0; i < services.size(); i++) {
					logger.info("Services Actions:" + services.get(i));
					String selectedAction = services.get(i);
					List<CommandTemplates> commandsList = commandTemplatesService
							.getAllCommandTemplatesByCode(selectedAction);
					CommandTemplates cmdTemplate = null;

					if (commandsList != null && commandsList.size() > 0) {
						CommandParams commandParams = new CommandParams();
						commandParams.setCode(selectedAction);
						List<CommandParams> paramsList = commandParamsService
								.getAllCommandParamsByCode(commandParams);
						cmdTemplate = commandsList.get(0);

						String command = cmdTemplate.getCommand();
						String params = null;
						if (paramsList != null && paramsList.size() > 0) {
							params = getParams(envId, selectedAction,
									paramsList, errorMap);

							String result = "";
							List<String> valErrors = errorMap
									.get(selectedAction);
							if ((valErrors == null || valErrors.size() == 0)) {
								valErrors = new ArrayList<String>();
								if (params != null && !params.trim().equals("")) {
									result = executeBatchFile(command + params,
											valErrors);
								} else {
									result = executeBatchFile(command,
											valErrors);
								}
								List<String> resultList = new ArrayList<String>();
								resultList.add(result);
								resultMap.put(selectedAction, resultList);
								errorMap.put(selectedAction, valErrors);
							}
						}else{
							logger.error("Parameters not found for action:" + selectedAction);
							List<String> paramErrors = new ArrayList<String>();
							paramErrors.add("Parameters not found for action:" + selectedAction);
							errorMap.put("Parameter Error", paramErrors);
						}
					} else {
						logger.error("Command not found for action:"+ selectedAction);
						List<String> resultList = new ArrayList<String>();
						resultList.add("");
						resultMap.put(selectedAction, resultList);

						List<String> valErrors = new ArrayList<String>();
						valErrors.add("Command is not configured action:"
								+ selectedAction);
						errorMap.put(selectedAction, valErrors);
					}
				}
			}
			resultAndErrorMap.put("result", resultMap);
			resultAndErrorMap.put("error", errorMap);
		} catch (Exception e) {
			logger.error("Error while processinf command.", e);
		}
		logger.info("Executed processdeDloymentOptionsService method:"
				+ env_p.getDeploymentServices());
		return resultAndErrorMap;
	}

	private String getParams(String envName, String selectedAction,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		String commandParams = "";
		// StopServer hostname ServiceName LogFilePath
		// StartServer hostname ServiceName LogFilePath
		// Imrep userid password ODBC RepositoryName ImportFilePath LogFilePath
		// Rename_CopySRF SrfSourcePath LogFilePath
		// DDLSynch userid password ODBC logfilepath repository siebelpwd
		// siebeldata siebelindex logfilepath
		// CopySRFBS SourceBSPath LogiflePath
		// ExportRep userid password ODBC RepositoryName EXportFilePath
		// LogFilePath
		if (selectedAction.equals("BuildNow")) {
			commandParams = buildNowCommandParams(selectedAction, envName, paramsList,errorMap);
		} else if (selectedAction.equals("StopServer")) {
			commandParams = stopServerCommandParams(selectedAction,envName, paramsList, errorMap);
		} else if (selectedAction.equals("StartServer")) {
			commandParams = startServerCommandParams(selectedAction,envName, paramsList,errorMap);
		} else if (selectedAction.equals("Imrep")) {
			commandParams = imrepCommandParams(selectedAction,envName, paramsList, errorMap);
		} else if (selectedAction.equals("RenameRespository")) {
			commandParams = renameRepoCommandParams(selectedAction,envName, paramsList, errorMap);
		} else if (selectedAction.equals("ApplySchemaChanges")) {
			commandParams = ddlSyncCommandParams(selectedAction,envName, paramsList, errorMap);
		} else if (selectedAction.equals("CopySRFBS")) {
			commandParams = copySRFBSCommandParams(selectedAction,envName, paramsList, errorMap);
		} else if (selectedAction.equals("Exprep")) {
			commandParams = exportRepCommandParams(selectedAction,envName, paramsList,errorMap);
		} else if (selectedAction.equals("ADMExport")) {
			commandParams = admExportCommandParams(selectedAction,envName, paramsList,	errorMap);
		} else if (selectedAction.equals("ADMImport")) {
			commandParams = admImportCommandParams(selectedAction,envName, paramsList,	errorMap);
		} else if (selectedAction.equals("CopyWebTemplate")) {
			commandParams = copyWebTemplateCommandParams(selectedAction,envName, paramsList,errorMap);
		} else if (selectedAction.equals("MigrateSRFRepositoryNow")) {
			commandParams = migrateSRFRepositoryNowCommandParams(selectedAction,envName, paramsList,errorMap);
		} 
		else {
			logger.error("Selected screen action did not configured in the backend:"
					+ selectedAction);
		}

		return commandParams;
	}

	private String migrateSRFRepositoryNowCommandParams(String selectedAction, String envName,
			List<CommandParams> paramsList,HashMap<String, List<String>> errorMap) {
		List<String> errors = migrateSRFRepositoryNowCommandValidate(envName, paramsList);
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}		
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("SourcePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				} else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Env. Info. is empty for "+ envName);
			errors.add("Error command did not constrcuted properly. Env. Info. is empty for "+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private List<String> migrateSRFRepositoryNowCommandValidate(String envName, List<CommandParams> paramsList) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = new ArrayList<String>();
			String sourcePath = envInfo.getMigrationPath();
		
			if(envName == null){
				errors.add("Environment name is missing");
			}
			if(paramsList == null){
				errors.add("Parameters list missing");
			}
						
		if (envInfo != null) {

			if (sourcePath == null) {
				errors.add("Migration Path   is missing in ENV Info.");
			}
		} else {
			errors.add("Env Info is not found.");
		}
			return errors;
	}

	private String copyWebTemplateCommandParams(String selectedAction, String envName, List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = copyWebTemplateCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				} else if (param.equals("SourcePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly.Env Info is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Env Info is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private List<String> copyWebTemplateCommandValidate(String envName, List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}
		if (envInfo != null) {
			String logFilePath = envInfo.getLogFilePath();
			String seibelPath = envInfo.getSeibelPath();
			String migrationPath = envInfo.getMigrationPath();
			if(logFilePath == null){
				errors.add("logFilePath is missing in Env Config");
			}
			if(seibelPath == null){
				errors.add("seibelPath Path is missing in Env Config");
			}
			if(migrationPath == null){
				errors.add("migrationPath is missing in Env Config");
			}
		} else {
			logger.error("ENV info. Config is missing for env:"+ envName);
			errors.add("ENV info. Config is missing for env:"+ envName);
		}
		return errors;
	}

	private String admImportCommandParams(String selectedAction, String envName, List<CommandParams> paramsList,
			 HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		AdmConfig admConfig = admConfigService.getAdmConfigByEnvName(envName);
		List<String> errors = admImportCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && admConfig != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				} else if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				}else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				}else if (param.equals("GatewayName")) {
					sb.append(" ").append(envInfo.getHostName());
				}else if (param.equals("EnterpriseName")) {
					sb.append(" ").append(envInfo.getEnterpriseName());
				} else if (param.equals("SiebelServer")) {
					sb.append(" ").append(admConfig.getSeibelServer());
				} else if (param.equals("SessionId")) {
					sb.append(" ").append(admConfig.getSessionId());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}  
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config or Env info or Adm Config are empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config or Env info or Adm Config are empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private String admExportCommandParams(String selectedAction, String envName, List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		AdmConfig admConfig = admConfigService.getAdmConfigByEnvName(envName);
		List<String> errors = admExportCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && admConfig != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				} else if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				}else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				}else if (param.equals("GatewayName")) {
					sb.append(" ").append(envInfo.getHostName());
				}else if (param.equals("EnterpriseName")) {
					sb.append(" ").append(envInfo.getEnterpriseName());
				} else if (param.equals("SiebelServer")) {
					sb.append(" ").append(admConfig.getSeibelServer());
				} else if (param.equals("SessionId")) {
					sb.append(" ").append(admConfig.getSessionId());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}  
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config or Env info or Adm Config are empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config or Env info or Adm Config are empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private List<String> admExportCommandValidate(String envName, List<CommandParams> paramsList) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		AdmConfig admConfig = admConfigService.getAdmConfigByEnvName(envName);
		List<String> errors = new ArrayList<String>();
			String seibelPath = envInfo.getSeibelPath();
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String hostName = envInfo.getHostName();
			String enterpriseName = envInfo.getEnterpriseName();
			String siebelServer = admConfig.getSeibelServer();
			String sessionId = admConfig.getSessionId();
			String logFilePath = envInfo.getLogFilePath();
			
			if(envName == null){
				errors.add("Environment name is missing");
			}
			if(paramsList == null){
				errors.add("Parameters list missing");
			}
						
			if(repo == null){
				errors.add("Repository Info is not found.");
			}else if(envInfo == null){
				errors.add("Env Info is not found.");
			}else if(admConfig == null){
				errors.add("AdmConfig Info is not found.");
			} else{
				if(seibelPath == null){
					errors.add("SeibelPath  is missing in ENV Info.");
				}
				if(userId == null){
					errors.add("UserId  is missing in Repo config.");
				}
				if(password == null){
					errors.add("password is missing in Repo config.");
				}
				if(hostName == null){
					errors.add("host name is missing in Env Info.");
				}
				if(enterpriseName == null){
					errors.add("Enterprise Name is missing in Env. Info.");
				}
				if(siebelServer == null){
					errors.add("Siebel Server is missing in ADM Config");
				}
				if(sessionId == null){
					errors.add("Session Id is missing in ADM Config.");
				}
				if(logFilePath == null){
					errors.add("Env. Info. is missing Log file path.");
				}
			}
			return errors;
	}
	private List<String> admImportCommandValidate(String envName, List<CommandParams> paramsList) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		AdmConfig admConfig = admConfigService.getAdmConfigByEnvName(envName);
		List<String> errors = admExportCommandValidate(envName, paramsList);
			String seibelPath = envInfo.getSeibelPath();
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String hostName = envInfo.getHostName();
			String enterpriseName = envInfo.getEnterpriseName();
			String siebelServer = admConfig.getSeibelServer();
			String sessionId = admConfig.getSessionId();
			String logFilePath = envInfo.getLogFilePath();
			
			if(envName == null){
				errors.add("Environment name is missing");
			}
			if(paramsList == null){
				errors.add("Parameters list missing");
			}
						
			if(repo == null){
				errors.add("Repository Info is not found.");
			}else if(envInfo == null){
				errors.add("Env Info is not found.");
			}else if(admConfig == null){
				errors.add("AdmConfig Info is not found.");
			} else{
				if(seibelPath == null){
					errors.add("SeibelPath  is missing in ENV Info.");
				}
				if(userId == null){
					errors.add("UserId  is missing in Repo config.");
				}
				if(password == null){
					errors.add("password is missing in Repo config.");
				}
				if(hostName == null){
					errors.add("host name is missing in Env Info.");
				}
				if(enterpriseName == null){
					errors.add("Enterprise Name is missing in Env. Info.");
				}
				if(siebelServer == null){
					errors.add("Siebel Server is missing in ADM Config");
				}
				if(sessionId == null){
					errors.add("Session Id is missing in ADM Config.");
				}
				if(logFilePath == null){
					errors.add("Env. Info. is missing Log file path.");
				}
			}
			return errors;
	}
	private String buildNowCommandParams(String selectedAction, String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<String> errors = buildNowValidate(envName, paramsList);
		EnvInfo envInfo = null;
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				} else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				} else if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		} else {
			logger.error("Error command did not constrcuted properly. Repository or envInfo is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repository or envInfo is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		
		return sb.toString();
	}
	@SuppressWarnings("unused")
	private List<String> buildNowValidate(String envName,
			List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		
		EnvInfo envInfo = null;
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		
		if(repo == null){
			errors.add("Repository Info is not found.");
		}else if(envInfo == null){
			errors.add("Env Info is not found.");
		} else{
			if(repo.getUserId() == null){
				errors.add("Repository Info is missing User Id.");
			}
			if(repo.getPassword() == null){
				errors.add("Repository Info is missing password.");
			}
			if(envInfo.getLogFilePath() == null){
				errors.add("Env. Info. is missing Log file path.");
			}
			if(envInfo.getSeibelPath() == null){
				errors.add("Siebelpath is missing Log file path.");
			}
		}
		return errors;
	}
	private List<String> stopServerCommandValidate(String envName, List<CommandParams> paramsList) {
		EnvInfo envInfo = null;
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}

		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
			String hostName = envInfo.getHostName();
			String serviceName = envInfo.getService();
			String logFilePath = envInfo.getLogFilePath();
			if (hostName == null) {
				errors.add("HostName is missing in environment info:"+envName+" "+hostName);
			}
			if (serviceName == null) {
				errors.add("Service Name is missing in environment info:"+envName+" "+serviceName);
			}
			if (logFilePath == null) {
				errors.add("LogFile path is missing in environment info:"+envName+" "+logFilePath);
			}
		}else{
			errors.add("Environment info missing. Please verify selected environment:"+envName);
			logger.error("Environment info missing. Please verify selected environment:"+envName);
		}		
		return errors;
	}
	public List<String> startServerCommandValidate(String envName,
			List<CommandParams> paramsList) {
		EnvInfo envInfo = null;
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
			if (envInfo.getHostName() == null) {
				errors.add("Host Name is missing in ENV setup");
			}
			if (envInfo.getService() == null) {
				errors.add("Service Name is missing in ENV setup");
			}
			if (envInfo.getLogFilePath() == null) {
				errors.add("LogFile is missing in ENV setup");
			}
		} else {
			logger.error("EnvInfo is missing for " + envName);
			errors.add("EnvInfo is missing.");
		}
		return errors;
	}
	private List<String> imrepCommandValidate(String envName,
			List<CommandParams> paramsList) {

		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}

		
		if (repo != null && envInfo != null) {
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String migrationFilePath = envInfo.getMigrationPath();
			String logFilePath = envInfo.getLogFilePath();
			String Siebelpath = envInfo.getSeibelPath();
			if (userId == null) {
				errors.add("User Id is missing in Repository Config");
			}
			if (password == null) {
				errors.add("Password is missing in Repository Config");
			}
			if (odbc == null) {
				errors.add("Odbc is missing in Repository Config");
			}
			if (repoName == null) {
				errors.add("Repo Name is missing in Repository Config");
			}
			if(migrationFilePath == null){
				errors.add("migrationFilePath/SourceFilePath is missing in Env. info Config");
			}
			if (logFilePath == null) {
				errors.add("Log File Path is missing in Env. Info. Config");
			}
			if(Siebelpath == null){
				errors.add("Siebelpath  is missing in Env. Info.");
			}
		} else {
			logger.error("Repository is empty for "+ envName);
			errors.add("Repository Config is missing.");
		}
		return errors;
	}
	private List<String> renameRepoCommandValidate(String envName,
		List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}		
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if (repo != null && envInfo != null) {
			String userId =repo.getUserId();
			String password =repo.getPassword();
			String odbc =repo.getOdbc();
			String logFilePath = envInfo.getLogFilePath();
			
			if(userId == null){
				errors.add("userId is missing in Repo Config.");
			}
			if(password == null){
				errors.add("password is missing in Repo Config.");
			}
			if(odbc == null){
				errors.add("odbc is missing in Repo Config.");
			}
			if(logFilePath == null){
				errors.add("Log file path is missing in Env. Info. Config");
			}
		} else {
			logger.error("Repository or Env. info is empty for "
					+ envName);
			errors.add("Repository Config is missing.");
		}
		return errors;
	}
	private List<String> exportRepCommandValidate(String envName,
			List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}

		if (repo != null) {
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String logFilePath = envInfo.getLogFilePath();
			String migrationPath = envInfo.getMigrationPath();
			String Siebelpath = envInfo.getSeibelPath();
			if(userId == null){
				errors.add("User Id is missing in Repository Config");
			}
			if(password == null){
				errors.add("password is missing in Repository Config");
			}
			if(odbc == null){
				errors.add("odbc is missing in Repository Config");
			}
			if(repoName == null){
				errors.add("Repo Name is missing in Repository Config");
			}
			if(logFilePath == null){
				errors.add("LogFile Path is missing in Env. Info. Config");
			}
			if(migrationPath == null){
				errors.add("MigrationPath Path is missing in Env. Info. Config");
			}
			if(Siebelpath == null){
				errors.add("Siebelpath Path is missing in Env. Info.");
			}
			
		} else {
			logger.error("Repository Config is missing for env:"+ envName);
			errors.add("Repository Config is missing for env:"+ envName);
		}
		return errors;
	}
	// StopServer hostname ServiceName LogFilePath
	private String stopServerCommandParams(String selectedAction, String envName,
			List<CommandParams> paramsList,  HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = stopServerCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("hostname")) {
					sb.append(" ").append(envInfo.getHostName());
				}else
				if (param.equals("ServiceName")) {
					sb.append(" ").append(envInfo.getService());
				}else
				if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly.  Env. Info. is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly.  Env. Info. is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// StartServer hostname ServiceName LogFilePath
	public String startServerCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		List<String> errors = startServerCommandValidate(envName, paramsList);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("hostname")) {
					sb.append(" ").append(envInfo.getHostName());
				} else if (param.equals("ServiceName")) {
					sb.append(" ").append(envInfo.getService());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// Imrep userid password ODBC RepositoryName ImportFilePath LogFilePath
	private String imrepCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList,  HashMap<String, List<String>> errorMap) {

		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<String> errors = imrepCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				} else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				} else if (param.equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				} else if (param.equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				} else if (param.equals("SourceFilePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				}else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config or Env info is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config or Env is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private String renameRepoCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<String> errors = renameRepoCommandValidate(envName, paramsList);
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}		
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				} else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				} else if (param.equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config or Env. Info. is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config or Env. Info.  is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// DDLSynch userid password ODBC logfilepath repository siebelpwd siebeldata
	// siebelindex logfilepath
	private String ddlSyncCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		List<String> errors = ddlSyncCommandValidate(envName, paramsList);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				} else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				} else if (param.equals("TableOwner")) {
					sb.append(" ").append(repo.getTableOwner());
				} else if (param.equals("TableOwnerPassword")) {
					sb.append(" ").append(repo.getTableOwnerPassword());
				} else if (param.equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				} else if (param.equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				} else if (param.equals("siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				} else if (param.equals("siebeldata")) {
					sb.append(" ").append(repo.getTableDDLSync());
				} else if (param.equals("siebelindex")) {
					sb.append(" ").append(repo.getIndexDDLSync());
				}else if (param.equals("SourceFilePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				} else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}
	private List<String> ddlSyncCommandValidate(String envName,
			List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}

		if (repo != null && envInfo != null) {
			String siebelpath = envInfo.getSeibelPath();
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String siebeldata = repo.getTableDDLSync();
			String siebelIndex = repo.getIndexDDLSync();
			String logFilePath = envInfo.getLogFilePath();
			String tableOwner = repo.getTableOwner();
			String tableOwnerPassword = repo.getTableOwnerPassword();
			String migrationPath = envInfo.getMigrationPath();
			if(userId == null){
				errors.add("User Id is missing in Repository Config");
			}
			if(password == null){
				errors.add("password is missing in Repository Config");
			}
			if(tableOwner == null){
				errors.add("Table Owner is missing in Repository Config");
			}
			if(tableOwnerPassword == null){
				errors.add("Table Owner Password is missing in Repository Config");
			}
			if(odbc == null){
				errors.add("odbc is missing in Repository Config");
			}
			if(repoName == null){
				errors.add("Repo Name is missing in Repository Config");
			}
			if(logFilePath == null){
				errors.add("LogFile Path is missing in Env. info.");
			}
			if(siebeldata == null){
				errors.add("Siebeldata is missing in Repository Config");
			}
			if(siebelIndex == null){
				errors.add("SeibelIndex is missing in Repository Config");
			}
			if(siebelpath == null){
				errors.add("siebelpath is missing in Env. Info. Config");
			}
			if(migrationPath == null){
				errors.add("migrationPath is missing in Env. Info. Config");
			}
		} else {
			logger.error("Repository Config and Env. Info. is missing for env:"+ envName);
			errors.add("Repository Config and Env. Info. is missing for env:"+ envName);
		}
		return errors;
	}
	// CopySRFBS SourceBSPath LogiflePath
	private String copySRFBSCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = copySRFBSCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				} else if (param.equals("SourcePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly.Env Info is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Env Info is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}
	private List<String> copySRFBSCommandValidate(String envName,
			List<CommandParams> paramsList) {
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}
		if (envInfo != null) {
			String logFilePath = envInfo.getLogFilePath();
			String seibelPath = envInfo.getSeibelPath();
			String migrationPath = envInfo.getMigrationPath();
			if(logFilePath == null){
				errors.add("logFilePath is missing in Env Config");
			}
			if(seibelPath == null){
				errors.add("seibelPath Path is missing in Env Config");
			}
			if(migrationPath == null){
				errors.add("migrationPath is missing in Env Config");
			}
		} else {
			logger.error("ENV info. Config is missing for env:"+ envName);
			errors.add("ENV info. Config is missing for env:"+ envName);
		}
		return errors;
	}
	// ExportRep userid password ODBC RepositoryName EXportFilePath LogFilePath
	private String exportRepCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvName(envName);
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = exportRepCommandValidate(envName, paramsList);
		StringBuffer sb = new StringBuffer();
		if (repo != null && envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				String param = commandParam.getParam();
				if (param.equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				} else if (param.equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				} else if (param.equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				} else if (param.equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				} else if (param.equals("MigrationFilePath")) {
					sb.append(" ").append(envInfo.getMigrationPath());
				} else if (param.equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				} else if (param.equals("Siebelpath")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				}else{
					errors.add("command parameter is configured. Verify configuration:"+ param);
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config or env Info is empty for "
					+ envName);
			errors.add("Error command did not constrcuted properly. Repo config or env info is empty for "
					+ envName);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	private String executeCommand(String command) {
		logger.info("Executing executeCommand method:" + command);
		Process p;
		StringBuilder sb = new StringBuilder();
		try {
			System.out.println("Executing the command:" + command);
			p = Runtime.getRuntime().exec(command);
			InputStream error = p.getErrorStream();
			InputStream output = p.getInputStream();
			String errorString = getStringFromInputStream(error);
			String outputString = getStringFromInputStream(output);
			sb.append(errorString);
			sb.append(System.lineSeparator());
			sb.append(outputString);
			logger.info("Executed executeCommand method:" + command);
			return sb.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Executed executeCommand method:" + command, e);
			return null;
		}
	}

	private String applyTokenValuesForCommand(String command,
			VelocityContext context) {
		Velocity.init();
		// String template = "Hello $name. Please find attached invoice"
		// + " $invoiceNumber which is due on $dueDate.";
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "TemplateName", command);
		return writer.toString();
	}

	private VelocityContext getTokenMaps(EnvInfo envInfo, Repos repos,
			AdmConfig admConfig) {
		// Token Maps
		VelocityContext tokenMaps = new VelocityContext();

		// ADM_CONFIG
		if (admConfig != null) {
//			tokenMaps.put(DMCommandTokens.ADM_CONFIG_USER_ID,admConfig.getUserId());
//			tokenMaps.put(DMCommandTokens.ADM_CONFIG_PASSWORD,admConfig.getPassword());
//			tokenMaps.put(DMCommandTokens.ADM_CONFIG_LOG_FILE_PATH,admConfig.getLogFilePath());
			tokenMaps.put(DMCommandTokens.SEIBEL_SERVER,
					admConfig.getSeibelServer());
			tokenMaps.put(DMCommandTokens.ROW_ID, admConfig.getSessionId());
		}
		if (repos != null) {
			// REPO
			tokenMaps.put(DMCommandTokens.REPO_USER_ID, repos.getUserId());
			tokenMaps.put(DMCommandTokens.REPO_PASSWORD, repos.getPassword());
			tokenMaps.put(DMCommandTokens.ODBC, repos.getOdbc());
			tokenMaps.put(DMCommandTokens.FILE_PATH, envInfo.getMigrationPath());
			tokenMaps
					.put(DMCommandTokens.LOG_FILE_PATH, envInfo.getLogFilePath());

			tokenMaps.put(DMCommandTokens.REPO_NAME, repos.getRepoName());
			tokenMaps
					.put(DMCommandTokens.TableDDLSync, repos.getTableDDLSync());
			tokenMaps
					.put(DMCommandTokens.IndexDDLSync, repos.getIndexDDLSync());
		}
		// ENV_INFO
		if (envInfo != null) {
			tokenMaps.put(DMCommandTokens.SERVICE_NAME, envInfo.getService());
			tokenMaps.put(DMCommandTokens.SEIBEL_PATH, envInfo.getSeibelPath());
			tokenMaps.put(DMCommandTokens.ADM_PATH, envInfo.getMigrationPath());
			tokenMaps.put(DMCommandTokens.HOST_NAME, envInfo.getServerHost());
		}
		return tokenMaps;

	}

//	@Override
//	public List<DeploymentOptions> getAllDeploymentPackages() {
//		//return deloymentOptionsDao.getAllDeploymentOptionsByCategory("package");
//		compTemplService.getAllCommTemplByUIScreenLocation("package");
//	}
	@Override
	public List<CommandTemplates> getAllDeploymentPackages() {
		//return deloymentOptionsDao.getAllDeploymentOptionsByCategory("package");
		return compTemplService.getAllCommTemplByUIScreenLocation("package");
	}
//	@Override
//	public List<DeploymentOptions> getAllDeploymentOptions() {
//		// TODO Auto-generated method stub
//		return deloymentOptionsDao.getAllDeploymentOptionsByCategory("Option");
//	}
	@Override
	public List<CommandTemplates> getAllDeploymentOptions() {
		// TODO Auto-generated method stub
		return compTemplService.getAllCommTemplByUIScreenLocation("Option");
	}
	private Map<String, EnvInfo> getServerNamesByEnv(String selectedEnvName) {
		// EnvNames
		List<EnvInfo> envInfoList = envService.getAllEnvNames();
		Map<String, EnvInfo> envNameServerNameEnvInfo = new HashMap<String, EnvInfo>();
		// For each env Name find the all the servers
		for (int i = 0; i < envInfoList.size(); i++) {
			EnvInfo envInfo = envInfoList.get(i);
			String envName = envInfo.getName();
			if (selectedEnvName.equals(envName)) {
				List<EnvInfo> serversInfoForEnvList = envService
						.getAllEnvByEnvName(envName);
				for (int j = 0; j < serversInfoForEnvList.size(); j++) {
					EnvInfo envInfoForServer = serversInfoForEnvList.get(j);
					String env = envInfoForServer.getName();
					String hostName = envInfoForServer.getHostName();
					String key = env + "#" + hostName;
					envNameServerNameEnvInfo.put(key, envInfoForServer);
				}
			}
		}
		return envNameServerNameEnvInfo;
	}

	private String getStringFromInputStream(InputStream is) {
		if (true) {
			return "";
		}
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		try {

			br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	private String executeCommandWithAgent(String command) throws IOException {
		// in databasevalue is // sc //$HOST_NAME stop //$SERVICE_NAME
		String POST_URL = "http://localhost:8080/SDMToolAgent/rest/executeCommand/";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL);
		StringEntity entity = new StringEntity(command);
		httpPost.setEntity(entity);
		// httpPost.setHeader("Accept", "application/json");
		// httpPost.setHeader("Content-type", "application/json");

		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		client.close();
		return "executed";

	}

	private String executeBatchFile(String command, List<String> valErrors) {
		logger.info("Executing executeBatchFile:" + command);
		Process p;
		StringBuilder sb = new StringBuilder();
		try {
			logger.info("Executing executeBatchFile with :" + command);
			p = Runtime.getRuntime().exec(command);
			InputStream error = p.getErrorStream();
			InputStream output = p.getInputStream();
			String errorString = getStringFromInputStream(error);
			String outputString = getStringFromInputStream(output);
			sb.append(errorString);
			sb.append(System.lineSeparator());
			sb.append(outputString);
			logger.info("Executed executeBatchFile with :" + command);
			return sb.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			valErrors.add(e.getMessage());	
			e.printStackTrace();
			logger.error("Executed executeBatchFile:" + command, e);
		}
		logger.info("Executed executeBatchFile:" + command);
		return sb.toString();
	}
}
