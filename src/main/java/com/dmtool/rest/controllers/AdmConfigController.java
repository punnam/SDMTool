package com.dmtool.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.dmtool.domain.AdmConfig;
import com.dmtool.domain.EnvInfo;
import com.dmtool.domain.Repos;
import com.dmtool.services.AdmConfigService;
import com.dmtool.services.EnvService;
import com.dmtool.services.ReposService;
import com.dmtool.user.actions.AdmServicesActions;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class AdmConfigController extends RootController{
	@Autowired
	private EnvService envService;

	@Autowired
	private AdmConfigService admConfigService;
	
	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(EnvController.class);
	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
	}
	@RequestMapping(value = { "/rest/createAdmConfig/" }, method = { RequestMethod.POST })
	public ModelAndView createAdmConfig(@RequestBody AdmConfig admConfig,
			HttpServletResponse httpResponse_p, WebRequest request_p) {
		
		logger_c.debug("Creating Env: " + admConfig.toString());

		try {
			admConfigService.createAdmConfig(admConfig, CREATED_USER_ID);
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request_p.getContextPath() + "/rest/createAdminConfig/" + admConfig.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, admConfig);
	}
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/getAdmConfig/{id}", method = RequestMethod.GET)
	public ModelAndView getAdmConfig(@PathVariable("id") String id_p) {
		AdmConfig admConfig = null;

		/* validate fund Id parameter */
		if (isEmpty(id_p)) {
			String sMessage = "Error invoking getAdmConfig - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			int envId = Integer.parseInt(id_p);
			admConfig = admConfigService.getAdmConfigById(envId);
		} catch (Exception e) {
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + admConfig.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, admConfig);
	}
	
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/admConfig/deleteAdmConfig/", method = RequestMethod.POST)
	public ModelAndView deleteAdmConfig(@RequestBody AdmConfig admConfig) {
		
		/* validate fund Id parameter */
		if (admConfig == null) {
			String sMessage = "Error invoking deleteAdmConfig - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			//int envId = Integer.parseInt(id_p);
			admConfigService.deleteAdmConfig(admConfig);;
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + admConfig.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, admConfig);
	}
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/admConfig/modifyAdmConfig/", method = RequestMethod.POST)
	public ModelAndView modifyAdmConfig(@RequestBody AdmConfig admConfig) {
		
		/* validate fund Id parameter */
		if (admConfig == null) {
			String sMessage = "Error invoking admConfig - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			//int envId = Integer.parseInt(id_p);
			admConfigService.modifyAdmConfig(admConfig);
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + admConfig.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, admConfig);
	}
		
	@RequestMapping(value = "/rest/admConfig/getAllAdmConfig/", method = RequestMethod.GET)
	public ModelAndView getAllAdmConfig() {
		List<AdmConfig> admConfigList = null;

		try {
			admConfigList = admConfigService.getAllAdmConfig();
		} catch (Exception e) {
			String sMessage = "Error getting all funds. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Persons: " + admConfigList.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, admConfigList);
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
	
}
