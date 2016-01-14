package com.dmtool.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.dmtool.domain.DeploymentOptions;
import com.dmtool.services.DeploymentOptionsService;
import com.dmtool.services.EnvService;
import com.dmtool.user.actions.DeploymentOptionsActions;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class DeploymentOptionsController {
	@Autowired
	private DeploymentOptionsService deploymentOptionsService;

	@Autowired
	private EnvService envService;
	
	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(EnvController.class);
	
	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
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
	
	@RequestMapping(value = "/rest/deploymentOptions/getAllDeploymentOptions/", method = RequestMethod.GET)
	public ModelAndView getAllDeploymentOptions() {
		List<DeploymentOptions> deploymentOptionServices = null;

		try {
			deploymentOptionServices = deploymentOptionsService.getAllDeploymentOptions();
		} catch (Exception e) {
			String sMessage = "Error getting all Adm Build services. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Deployment Options: " + deploymentOptionServices.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, deploymentOptionServices);
	}


	@RequestMapping(value = "/rest/deploymentOptions/getAllDeploymentPackages/", method = RequestMethod.GET)
	public ModelAndView getAllDeploymentPackages() {
		List<DeploymentOptions> deploymentOptionServices = null;

		try {
			deploymentOptionServices = deploymentOptionsService.getAllDeploymentPackages();
		} catch (Exception e) {
			String sMessage = "Error getting all Adm Build services. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Deployment Options: " + deploymentOptionServices.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, deploymentOptionServices);
	}

	@RequestMapping(value = "/rest/deploymentOptions/processDeploymentOptions/", method = RequestMethod.POST)
	public ModelAndView processDeploymentOptions(@RequestBody DeploymentOptionsActions env_p,
			HttpServletResponse httpResponse_p, WebRequest request_p) {
		
		logger_c.debug("Creating Env: " + env_p.toString());
		String output ="";
		boolean commandStatus = false;
		try {
			 output = deploymentOptionsService.processdeDloymentOptionsService(env_p);
			 if(output != null || !output.equals("")){
				 commandStatus= true;
			 }
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request_p.getContextPath() + "/rest/deploymentOptions/processDeploymentOptions/" + env_p.getEnvName());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, commandStatus);
	}
}
