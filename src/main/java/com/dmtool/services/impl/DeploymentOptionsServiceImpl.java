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
	public List<DeploymentOptions> getAllDeploymentOptions() {
		// TODO Auto-generated method stub
		return deloymentOptionsDao.getAllDeploymentOptionsByCategory("Option");
	}

	/**
	 * @Override public String
	 *           processdeDloymentOptionsService(DeploymentOptionsActions env_p)
	 *           { String selectedEnvName = env_p.getEnvName(); String
	 *           actionType = env_p.getActionType(); StringBuilder sb = new
	 *           StringBuilder(); //REPO_CONFIG Repos repos =
	 *           reposService.getRepoInfoByEnvNameAndActionType
	 *           (selectedEnvName,actionType);
	 * 
	 *           //Need map for ADM_CONFIG#ENV_NAME AdmConfig admConfig =
	 *           admConfigService
	 *           .getAdmConfigByEnvNameAndActionType(selectedEnvName
	 *           ,actionType);
	 * 
	 * 
	 *           Map<String, EnvInfo> envNameServerNameEnvInfo =
	 *           getServerNamesByEnv(selectedEnvName);
	 * 
	 *           System.out.println("envName:"+selectedEnvName); List<String>
	 *           services = env_p.getDeploymentServices(); for (int i = 0; i <
	 *           services.size(); i++) {
	 *           System.out.println("Services Actions:"+services.get(i)); String
	 *           selectedAction = services.get(i); List<CommandTemplates>
	 *           commandsList =
	 *           commandTemplatesService.getAllCommandTemplatesByCode
	 *           (selectedAction); if(commandsList != null &&
	 *           commandsList.size()>0){ for (int j = 0; i <
	 *           commandsList.size(); j++) { CommandTemplates cmdTemplate =
	 *           commandsList.get(j); String command = cmdTemplate.getCommand();
	 *           Set<String> serverNames = envNameServerNameEnvInfo.keySet();
	 *           Iterator<String> serverNamesIter = serverNames.iterator();
	 *           while (serverNamesIter.hasNext()) { String key =
	 *           serverNamesIter.next();
	 * 
	 *           System.out.println("***Start****ServerName:"+key);
	 *           System.out.println("	Command before parsing:"+command);
	 * 
	 *           EnvInfo envInfo = envNameServerNameEnvInfo.get(key);
	 *           VelocityContext tokenMaps = getTokenMaps(envInfo, repos,
	 *           admConfig); String commandWithValues =
	 *           applyTokenValuesForCommand(command, tokenMaps);
	 * 
	 *           String consoleOutput= ""; try { //consoleOutput =
	 *           executeCommandWithAgent(commandWithValues); consoleOutput =
	 *           executeCommand(commandWithValues); return consoleOutput; }
	 *           catch (Exception e) { // TODO Auto-generated catch block
	 *           e.printStackTrace(); return null; }
	 * 
	 *           } } } } //Need map for ENV_NAME#SERVER_NAME
	 * 
	 *           //Need map for REPO_CONFIG#ENV_NAME#IMPORT //Need map for
	 *           REPO_CONFIG#ENV_NAME#EXPORT //Need map for
	 *           REPO_CONFIG#ENV_NAME#SYNCDDL
	 * 
	 *           //Need map for ADM_CONFIG#ENV_NAME#IMPORT //Need map for
	 *           ADM_CONFIG#ENV_NAME#EXPORT return sb.toString(); }
	 **/
	@Override
	public HashMap<String, HashMap<String, List<String>>> processdeDloymentOptionsService(DeploymentOptionsActions env_p) {
		logger.info("Executing processdeDloymentOptionsService method:"
				+ env_p.getDeploymentServices());
		HashMap<String, HashMap<String, List<String>>> resultAndErrorMap = new HashMap<String, HashMap<String, List<String>>>();
		StringBuilder sb = new StringBuilder();
		try {
			List<String> services = env_p.getDeploymentServices();
			String envName = env_p.getEnvName();
			String actionType = env_p.getActionType();
			if (services == null || !(services.size() > 0)) {
				logger.error("No actions selected.");
			}

			HashMap<String, List<String>> errorMap = new HashMap<String, List<String>>();
			HashMap<String, List<String>> resultMap = new HashMap<String, List<String>>();
			
			for (int i = 0; i < services.size(); i++) {
				logger.info("Services Actions:" + services.get(i));
				String selectedAction = services.get(i);
				List<CommandTemplates> commandsList = commandTemplatesService
						.getAllCommandTemplatesByCode(selectedAction);
				CommandTemplates cmdTemplate = null;
				List<CommandParams> paramsList = commandParamsService
						.getAllCommandParamsByCode(selectedAction);
				if (commandsList != null && commandsList.size() > 0) {
					cmdTemplate = commandsList.get(0);


				String command = cmdTemplate.getCommand();
				String params = null;
				if(paramsList != null && paramsList.size() >0 ){
					params = getParams(envName, selectedAction,
						paramsList, actionType, errorMap);
				}
				String result ="";
				List<String> valErrors = errorMap.get(selectedAction);
				if((valErrors == null || valErrors.size() ==0)){
					valErrors = new ArrayList<String>();
					if(params != null && !params.trim().equals("")){
						result = executeBatchFile(command + params,valErrors);
					}else{
						result = executeBatchFile(command,valErrors);
					}	
					List<String> resultList = new ArrayList<String>();
					resultList.add(result);
					resultMap.put(selectedAction, resultList);
					errorMap.put(selectedAction, valErrors);
				}
				} else {
					logger.error("Command not found for action:"+ selectedAction);
					List<String> resultList = new ArrayList<String>();
					resultList.add("");
					resultMap.put(selectedAction, resultList);
					
					List<String> valErrors = new ArrayList<String>();
					valErrors.add("Command is not configured action:"+ selectedAction);
					errorMap.put(selectedAction, valErrors);
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
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		String commandParams = "";
		// StopServer hostname ServiceName LogFilePath
		// StartServer hostname ServiceName LogFilePath
		// Imrep userid password ODBC RepositoryName ImportFilePath LogFilePath
		// Rename_CopySRF SrfSourcePath LogFilePath
		// DDLSynch userid password ODBC logfilepath repository siebelpwd
		// siebeldata siebelindex logfilepath
		// Copy_BS SourceBSPath LogiflePath
		// ExportRep userid password ODBC RepositoryName EXportFilePath
		// LogFilePath
		if (selectedAction.equals("BuildNow")) {
			commandParams = buildNowCommandParams(selectedAction, envName, paramsList,
					actionType,errorMap);
		} else if (selectedAction.equals("StopServer")) {
			commandParams = stopServerCommandParams(selectedAction,envName, paramsList,
					actionType,errorMap);
		} else if (selectedAction.equals("StartServer")) {
			commandParams = startServerCommandParams(selectedAction,envName, paramsList,
					actionType,errorMap);
		} else if (selectedAction.equals("Imrep")) {
			commandParams = imrepCommandParams(selectedAction,envName, paramsList, "Import",errorMap);
		} else if (selectedAction.equals("Rename_CopySRF")) {
			commandParams = renameCopySRFCommandParams(selectedAction,envName, paramsList,
					actionType,errorMap);
		} else if (selectedAction.equals("ApplySchemaChanges")) {
			commandParams = ddlSyncCommandParams(selectedAction,envName, paramsList, "DDLSync",errorMap);
		} else if (selectedAction.equals("Copy_BS")) {
			commandParams = copyBSCommandParams(selectedAction,envName, paramsList, actionType,errorMap);
		} else if (selectedAction.equals("Exprep")) {
			commandParams = exportRepCommandParams(selectedAction,envName, paramsList,
					"Export",errorMap);
		} else {
			logger.error("Selected screen action did not configured in the backend:"
					+ selectedAction);
		}

		return commandParams;
	}

	private String buildNowCommandParams(String selectedAction, String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		AdmConfig admConfig = admConfigService
				.getAdmConfigByEnvNameAndActionType(envName, actionType);
		List<String> errors = buildNowValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (admConfig != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("UserId")) {
					sb.append(" ").append(admConfig.getUserId());
				}
				if (commandParam.getParam().equals("Password")) {
					sb.append(" ").append(admConfig.getPassword());
				}
				if (commandParam.getParam().equals("LogFilePath")) {
					sb.append(" ").append(admConfig.getLogFilePath());
				}
			}
		} else {
			logger.error("Error command did not constrcuted properly. ADM config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. ADM config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		
		return sb.toString();
	}
	@SuppressWarnings("unused")
	private List<String> buildNowValidate(String envName,
			List<CommandParams> paramsList, String actionType) {
		List<String> errors = new ArrayList<String>();
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if(actionType == null){
			errors.add("Action Type is missing");
		}
		
		AdmConfig admConfig = admConfigService
				.getAdmConfigByEnvNameAndActionType(envName, actionType);
		
		if(admConfig == null){
			errors.add("ADM Config is not found.");
		}else{
			if(admConfig.getUserId() == null){
				errors.add("ADM Config is missing User Id.");
			}
			if(admConfig.getPassword() == null){
				errors.add("ADM Config is missing password.");
			}
			if(admConfig.getLogFilePath() == null){
				errors.add("ADM Config is missing Log file path.");
			}
		}
		return errors;
	}
	private List<String> stopServerCommandValidate(String envName,
			List<CommandParams> paramsList, String actionType) {
		EnvInfo envInfo = null;
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if(actionType == null){
			errors.add("Action Type is missing");
		}
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
			String hostName = envInfo.getHostName();
			String serverName = envInfo.getServerName();
			String filePath = envInfo.getLogFilePath();
			if (hostName != null) {
				errors.add("HostName is missing in environment info:"+envName+" "+hostName);
			}
			if (serverName != null) {
				errors.add("Server Name is missing in environment info:"+envName+" "+serverName);
			}
			if (filePath != null) {
				errors.add("File path is missing in environment info:"+envName+" "+filePath);
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
			if (envInfo.getServerName() == null) {
				errors.add("Server Name is missing in ENV setup");
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
			List<CommandParams> paramsList, String actionType) {

		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if(actionType == null){
			errors.add("Action Type is missing");
		}

		
		if (repo != null) {
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String logFilePath = repo.getLogFilePath();
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
			if (logFilePath == null) {
				errors.add("Log File Path is missing in Repository Config");
			}
		} else {
			logger.error("Repository is empty for "+ envName + " and " + actionType);
			errors.add("Repository Config is missing.");
		}
		return errors;
	}
	private List<String> renameCopySRFCommandValidate(String envName,
		List<CommandParams> paramsList, String actionType) {
		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		if(envName == null){
			errors.add("Environment name is missing");
		}
		if(paramsList == null){
			errors.add("Parameters list missing");
		}
		if(actionType == null){
			errors.add("Action Type is missing");
		}
		if (repo != null) {
			String filePath = repo.getFilePath();
			String logFilePath = repo.getLogFilePath();
			
			if(filePath == null){
				errors.add("File path is missing in Repository Config");
			}
			if(logFilePath == null){
				errors.add("Log file path is missing in Repository Config");
			}
		} else {
			logger.error("Repository is empty for "
					+ envName + " and " + actionType);
			errors.add("Repository Config is missing.");
		}
		return errors;
	}
	private List<String> exportRepCommandValidate(String envName,
			List<CommandParams> paramsList, String actionType) {
		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}
		if(actionType == null){
			errors.add("Action Type is missing.");
		} 
		if (repo != null) {
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String logFilePath = repo.getLogFilePath();
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
				errors.add("LogFile Path is missing in Repository Config");
			}
		} else {
			logger.error("Repository Config is missing for env:"+ envName);
			errors.add("Repository Config is missing for env:"+ envName);
		}
		return errors;
	}
	// StopServer hostname ServiceName LogFilePath
	private String stopServerCommandParams(String selectedAction, String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		List<String> errors = stopServerCommandValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("hostname")) {
					sb.append(" ").append(envInfo.getHostName());
				}
				if (commandParam.getParam().equals("ServiceName")) {
					sb.append(" ").append(envInfo.getServerName());
				}
				if (commandParam.getParam().equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// StartServer hostname ServiceName LogFilePath
	public String startServerCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
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
				if (commandParam.getParam().equals("hostname")) {
					sb.append(" ").append(envInfo.getHostName());
				}
				if (commandParam.getParam().equals("ServiceName")) {
					sb.append(" ").append(envInfo.getServerName());
				}
				if (commandParam.getParam().equals("LogFilePath")) {
					sb.append(" ").append(envInfo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// Imrep userid password ODBC RepositoryName ImportFilePath LogFilePath
	private String imrepCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		List<String> errors = imrepCommandValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				}
				if (commandParam.getParam().equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				}
				if (commandParam.getParam().equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				}
				if (commandParam.getParam().equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				}
				if (commandParam.getParam().equals("ImportFilePath")) {
					sb.append(" ").append(repo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// Rename_CopySRF SrfSourcePath LogFilePath
	private String renameCopySRFCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		List<String> errors = renameCopySRFCommandValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("SrfSourcePath")) {
					sb.append(" ").append(repo.getFilePath());
				}
				if (commandParam.getParam().equals("LogFilePath")) {
					sb.append(" ").append(repo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}

	// DDLSynch userid password ODBC logfilepath repository siebelpwd siebeldata
	// siebelindex logfilepath
	private String ddlSyncCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		List<String> errors = ddlSyncCommandValidate(envName, paramsList, actionType);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName, actionType);
		StringBuffer sb = new StringBuffer();
		if (envInfo != null && repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				}
				if (commandParam.getParam().equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				}
				if (commandParam.getParam().equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				}
				if (commandParam.getParam().equals("LogFilePath")) {
					sb.append(" ").append(repo.getLogFilePath());
				}
				if (commandParam.getParam().equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				}
				if (commandParam.getParam().equals("siebelpwd")) {
					sb.append(" ").append(repo.getPassword());
				}
				if (commandParam.getParam().equals("siebeldata")) {
					sb.append(" ").append(repo.getPassword());
				}
				if (commandParam.getParam().equals("siebelindex")) {
					sb.append(" ").append(envInfo.getSeibelPath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}
	private List<String> ddlSyncCommandValidate(String envName,
			List<CommandParams> paramsList, String actionType) {
		List<String> errors = new ArrayList<String>();
		List<EnvInfo> envList = envService.getAllEnvByEnvName(envName);
		EnvInfo envInfo = null;
		if (envList != null && envList.size() > 0) {
			envInfo = envList.get(0);
		}
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}
		if(actionType == null){
			errors.add("Action Type is missing.");
		} 
		if (repo != null) {
			String userId = repo.getUserId();
			String password = repo.getPassword();
			String odbc = repo.getOdbc();
			String repoName = repo.getRepoName();
			String logFilePath = repo.getLogFilePath();
			String siebelpwd = repo.getFilePath();
			String seibelIndex = envInfo.getSeibelPath();
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
				errors.add("LogFile Path is missing in Repository Config");
			}
			if(siebelpwd == null){
				errors.add("Siebelpwd is missing in Repository Config");
			}
			if(seibelIndex == null){
				errors.add("SeibelIndex is missing in Repository Config");
			}
		} else {
			logger.error("Repository Config is missing for env:"+ envName);
			errors.add("Repository Config is missing for env:"+ envName);
		}
		return errors;
	}
	// Copy_BS SourceBSPath LogiflePath
	private String copyBSCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		List<String> errors = copyBSCommandValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("SourceBSPath")) {
					sb.append(" ").append(repo.getFilePath());
				}
				if (commandParam.getParam().equals("LogiflePath")) {
					sb.append(" ").append(repo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
		}
		if(errors != null && errors.size()>0){
			errorMap.put(selectedAction, errors);	
		}
		return sb.toString();
	}
	private List<String> copyBSCommandValidate(String envName,
			List<CommandParams> paramsList, String actionType) {
		List<String> errors = new ArrayList<String>();
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		if(envName == null){
			errors.add("Environment name is missing.");
		}
		if(paramsList == null){
			errors.add("Parameters list missing.");
		}
		if(actionType == null){
			errors.add("Action Type is missing.");
		} 
		if (repo != null) {
			String sourceBSPath = repo.getFilePath();
			String logFilePath = repo.getLogFilePath();
			if(sourceBSPath == null){
				errors.add("sourceBSPath is missing in Repository Config");
			}
			if(logFilePath == null){
				errors.add("LogFile Path is missing in Repository Config");
			}
		} else {
			logger.error("Repository Config is missing for env:"+ envName);
			errors.add("Repository Config is missing for env:"+ envName);
		}
		return errors;
	}
	// ExportRep userid password ODBC RepositoryName EXportFilePath LogFilePath
	private String exportRepCommandParams(String selectedAction,String envName,
			List<CommandParams> paramsList, String actionType, HashMap<String, List<String>> errorMap) {
		Repos repo = reposService.getRepoInfoByEnvNameAndActionType(envName,
				actionType);
		List<String> errors = exportRepCommandValidate(envName, paramsList, actionType);
		StringBuffer sb = new StringBuffer();
		if (repo != null && (errors == null || errors.size() == 0)) {
			for (Iterator iterator = paramsList.iterator(); iterator.hasNext();) {
				CommandParams commandParam = (CommandParams) iterator.next();
				if (commandParam.getParam().equals("UserId")) {
					sb.append(" ").append(repo.getUserId());
				}
				if (commandParam.getParam().equals("Password")) {
					sb.append(" ").append(repo.getPassword());
				}
				if (commandParam.getParam().equals("ODBC")) {
					sb.append(" ").append(repo.getOdbc());
				}
				if (commandParam.getParam().equals("RepositoryName")) {
					sb.append(" ").append(repo.getRepoName());
				}
				if (commandParam.getParam().equals("ImportFilePath")) {
					sb.append(" ").append(repo.getLogFilePath());
				}
			}
		}else {
			logger.error("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
			errors.add("Error command did not constrcuted properly. Repo config is empty for "
					+ envName + " and " + actionType);
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
			tokenMaps.put(DMCommandTokens.ADM_CONFIG_USER_ID,
					admConfig.getUserId());
			tokenMaps.put(DMCommandTokens.ADM_CONFIG_PASSWORD,
					admConfig.getPassword());
			tokenMaps.put(DMCommandTokens.ADM_CONFIG_LOG_FILE_PATH,
					admConfig.getLogFilePath());
			tokenMaps.put(DMCommandTokens.SEIBEL_SERVER,
					admConfig.getSeibelServer());
			tokenMaps.put(DMCommandTokens.ROW_ID, admConfig.getRowId());
		}
		if (repos != null) {
			// REPO
			tokenMaps.put(DMCommandTokens.REPO_USER_ID, repos.getUserId());
			tokenMaps.put(DMCommandTokens.REPO_PASSWORD, repos.getPassword());
			tokenMaps.put(DMCommandTokens.ODBC, repos.getOdbc());
			tokenMaps.put(DMCommandTokens.FILE_PATH, repos.getFilePath());
			tokenMaps
					.put(DMCommandTokens.LOG_FILE_PATH, repos.getLogFilePath());

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
			tokenMaps.put(DMCommandTokens.ADM_PATH, envInfo.getAdmPath());
			tokenMaps.put(DMCommandTokens.HOST_NAME, envInfo.getServerHost());
		}
		return tokenMaps;

	}

	@Override
	public List<DeploymentOptions> getAllDeploymentPackages() {
		return deloymentOptionsDao.getAllDeploymentOptionsByCategory("package");
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
