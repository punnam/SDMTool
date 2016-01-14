package com.dmtool.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.DeloymentOptionsDao;
import com.dmtool.domain.AdmConfig;
import com.dmtool.domain.AgentInfo;
import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.DeploymentOptions;
import com.dmtool.domain.EnvInfo;
import com.dmtool.domain.Repos;
import com.dmtool.services.AdmConfigService;
import com.dmtool.services.CommandTemplatesService;
import com.dmtool.services.DeploymentOptionsService;
import com.dmtool.services.EnvService;
import com.dmtool.services.ReposService;
import com.dmtool.user.actions.DeploymentOptionsActions;
import com.dmtool.utils.DMCommandTokens;

@Repository
public class DeploymentOptionsServiceImpl implements DeploymentOptionsService{
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
	
	@Override
	public List<DeploymentOptions> getAllDeploymentOptions() {
		// TODO Auto-generated method stub	
		return deloymentOptionsDao.getAllDeploymentOptionsByCategory("Option");
	}

	@Override
	public String processdeDloymentOptionsService(DeploymentOptionsActions env_p) {
		String selectedEnvName = env_p.getEnvName();
		String actionType = env_p.getActionType();
		StringBuilder  sb = new StringBuilder();
		//REPO_CONFIG
		Repos repos = reposService.getRepoInfoByEnvNameAndActionType(selectedEnvName,actionType);
		
		//Need map for ADM_CONFIG#ENV_NAME
		AdmConfig admConfig = admConfigService.getAdmConfigByEnvNameAndActionType(selectedEnvName,actionType);

		
		Map<String, EnvInfo> envNameServerNameEnvInfo = getServerNamesByEnv(selectedEnvName);
		
		System.out.println("envName:"+selectedEnvName);
		List<String> services = env_p.getDeploymentServices();
		for (int i = 0; i < services.size(); i++) {
			System.out.println("Services Actions:"+services.get(i));
			String selectedAction = services.get(i);
			List<CommandTemplates> commandsList = commandTemplatesService.getAllCommandTemplatesByCode(selectedAction);
			if(commandsList != null && commandsList.size()>0){
				for (int j = 0; i < commandsList.size(); j++) {
					CommandTemplates cmdTemplate = commandsList.get(j);
					String command = cmdTemplate.getCommand();
					Set<String> serverNames = envNameServerNameEnvInfo.keySet();
					Iterator<String> serverNamesIter = serverNames.iterator();
					while (serverNamesIter.hasNext()) {
						String key = serverNamesIter.next();
						
						System.out.println("***Start****ServerName:"+key);
						System.out.println("	Command before parsing:"+command);
						
						EnvInfo envInfo = envNameServerNameEnvInfo.get(key);
						VelocityContext tokenMaps = getTokenMaps(envInfo,
								repos, admConfig);
						String commandWithValues = applyTokenValuesForCommand(command, tokenMaps);
						
						String consoleOutput= "";
						try {
							//consoleOutput = executeCommandWithAgent(commandWithValues);
							consoleOutput = executeCommand(commandWithValues);
							return consoleOutput;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}

					}
				}
			}
		}
		//Need map for ENV_NAME#SERVER_NAME
		
		//Need map for REPO_CONFIG#ENV_NAME#IMPORT
		//Need map for REPO_CONFIG#ENV_NAME#EXPORT
		//Need map for REPO_CONFIG#ENV_NAME#SYNCDDL
		
		//Need map for ADM_CONFIG#ENV_NAME#IMPORT
		//Need map for ADM_CONFIG#ENV_NAME#EXPORT
		return sb.toString();
	}
 
	private String executeCommand(String command) {
		Process p;
		StringBuilder  sb = new StringBuilder();
		try {
			System.out.println("Executing the command:"+command);
			p = Runtime.getRuntime().exec(command);
			InputStream error = p.getErrorStream();
			InputStream output = p.getInputStream();
			String errorString = getStringFromInputStream(error);
			String outputString = getStringFromInputStream(output);
			sb.append(errorString);
			sb.append(System.lineSeparator());
			sb.append(outputString);
			System.out.println("Executed the command:"+command);
			return sb.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String applyTokenValuesForCommand(String command,
			VelocityContext context) {
		Velocity.init();
		//String template = "Hello $name. Please find attached invoice"
			//	+ " $invoiceNumber which is due on $dueDate.";
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "TemplateName", command);
		return writer.toString();
	}

	private VelocityContext getTokenMaps(EnvInfo envInfo, Repos repos, AdmConfig admConfig) {
		//Token Maps
		VelocityContext tokenMaps = new VelocityContext();

		//ADM_CONFIG
		if(admConfig != null){
		tokenMaps.put(DMCommandTokens.ADM_CONFIG_USER_ID,admConfig.getUserId());
		tokenMaps.put(DMCommandTokens.ADM_CONFIG_PASSWORD,admConfig.getPassword());
		tokenMaps.put(DMCommandTokens.ADM_CONFIG_LOG_FILE_PATH,admConfig.getLogFilePath());
		tokenMaps.put(DMCommandTokens.SEIBEL_SERVER,admConfig.getSeibelServer());
		tokenMaps.put(DMCommandTokens.ROW_ID,admConfig.getRowId());
		}
		if(repos != null){
		//REPO
		tokenMaps.put(DMCommandTokens.REPO_USER_ID,repos.getUserId());
		tokenMaps.put(DMCommandTokens.REPO_PASSWORD,repos.getPassword());
		tokenMaps.put(DMCommandTokens.ODBC,repos.getOdbc());
		tokenMaps.put(DMCommandTokens.FILE_PATH,repos.getFilePath());
		tokenMaps.put(DMCommandTokens.LOG_FILE_PATH,repos.getLogFilePath());
		
		tokenMaps.put(DMCommandTokens.REPO_NAME,repos.getRepoName());
		tokenMaps.put(DMCommandTokens.TableDDLSync,repos.getTableDDLSync());
		tokenMaps.put(DMCommandTokens.IndexDDLSync,repos.getIndexDDLSync());
		}
		//ENV_INFO
		if(envInfo != null){
		tokenMaps.put(DMCommandTokens.SERVICE_NAME,envInfo.getService());
		tokenMaps.put(DMCommandTokens.SEIBEL_PATH,envInfo.getSeibelPath());
		tokenMaps.put(DMCommandTokens.ADM_PATH,envInfo.getAdmPath());
		tokenMaps.put(DMCommandTokens.HOST_NAME,envInfo.getServerHost());
		}
		return tokenMaps;
		
	}

	@Override
	public List<DeploymentOptions> getAllDeploymentPackages() {
		return deloymentOptionsDao.getAllDeploymentOptionsByCategory("package");
	}
	private Map<String, EnvInfo> getServerNamesByEnv(String selectedEnvName){
		//EnvNames
		List<EnvInfo> envInfoList = envService.getAllEnvNames();
		Map<String, EnvInfo> envNameServerNameEnvInfo = new HashMap<String, EnvInfo>();
		//For each env Name find the all the servers
		for (int i = 0; i < envInfoList.size(); i++) {
			EnvInfo envInfo = envInfoList.get(i);
			String envName =  envInfo.getName();
			if(selectedEnvName.equals(envName)){
				List<EnvInfo> serversInfoForEnvList = envService.getAllEnvByEnvName(envName);
				for (int j = 0; j < serversInfoForEnvList.size(); j++) {
					EnvInfo envInfoForServer = serversInfoForEnvList.get(j);
					String env = envInfoForServer.getName();
					String hostName = envInfoForServer.getHostName();
					String key = env+ "#" + hostName;
					envNameServerNameEnvInfo.put(key, envInfoForServer);	
				}
			}
		}
		return envNameServerNameEnvInfo;
	}
	private  String getStringFromInputStream(InputStream is) {
		if(true){
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
		// in databasevalue is //   sc //$HOST_NAME stop //$SERVICE_NAME
		String POST_URL ="http://localhost:8080/SDMToolAgent/rest/executeCommand/"; 
		   CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(POST_URL);
		    StringEntity entity = new StringEntity(command);
		    httpPost.setEntity(entity);
		    //httpPost.setHeader("Accept", "application/json");
		    //httpPost.setHeader("Content-type", "application/json");
		 
		    CloseableHttpResponse response = client.execute(httpPost);
		    System.out.println(response.getStatusLine().getStatusCode());
		    System.out.println(response.getStatusLine().getReasonPhrase());
		    client.close();
		    return "executed";
 
    }
	private String executeBatchFile(String command) {
		Process p;
		StringBuilder  sb = new StringBuilder();
		try {
			String cmd="cmd /c start c:samplenote.bat"; 
			p = Runtime.getRuntime().exec(command);
			InputStream error = p.getErrorStream();
			InputStream output = p.getInputStream();
			String errorString = getStringFromInputStream(error);
			String outputString = getStringFromInputStream(output);
			sb.append(errorString);
			sb.append(System.lineSeparator());
			sb.append(outputString);
			return sb.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
