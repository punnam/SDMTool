package com.dmtool.rest.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.dmtool.domain.AgentInfo;
import com.dmtool.services.AgentInfoService;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class AgentInfoServerController extends RootController{

	@Autowired
	private AgentInfoService agentInfoService;
	
	@Autowired
	private View jsonView_i;
	
	private @Autowired HttpServletRequest request;
	
	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(AgentInfoServerController.class);
	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
	}
	
	@RequestMapping(value = "/rest/saveAgentInfo/", method = RequestMethod.POST, consumes="application/json")
	public ModelAndView saveAgentInfo(@RequestBody String jsonString) throws JsonGenerationException, JsonMappingException, IOException {
		AgentInfo agentInfo = null;
		if(jsonString != null){
		ObjectMapper mapper = new ObjectMapper();
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(jsonString);
		
		//JSON from String to Object
		agentInfo = mapper.readValue(jsonString, AgentInfo.class);
		
		agentInfoService.createAgentInfo(agentInfo, CREATED_USER_ID);
		logger_c.debug("Returing Agent Info: " + agentInfo.toString());
		}
		return new ModelAndView(jsonView_i, DATA_FIELD, agentInfo);
	}


	/**
	 * Create an error REST response.
	 *
	 * @param sMessage
	 *            the s message
	 * @return the model and view
	 */
	private ModelAndView createErrorResponse(String sMessage) {
		return new ModelAndView(jsonView_i, ERROR_FIELD, sMessage);
	}
	
	/**
	 * Injector methods.
	 *
	 * @param view
	 *            the new json view
	 */
	public void setJsonView(View view) {
		jsonView_i = view;
	}
	private static void postCommand() throws IOException {
		 
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("POST_URL");
        httpPost.addHeader("User-Agent", "USER_AGENT");
 
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("userName", "Pankaj Kumar"));
 
        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        httpPost.setEntity(postParams);
 
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
 
        System.out.println("POST Response Status:: "
                + httpResponse.getStatusLine().getStatusCode());
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));
 
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
 
        // print result
        System.out.println(response.toString());
        httpClient.close();
 
    }
}