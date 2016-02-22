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

import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.EnvInfo;
import com.dmtool.services.CommandTemplatesService;
import com.dmtool.services.EnvService;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class CommandTemplateController extends RootController{

	@Autowired
	private CommandTemplatesService commTemplateService;

	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(CommandTemplateController.class);

	@RequestMapping(value = { "/rest/creatCommTempl/" }, method = { RequestMethod.POST })
	public ModelAndView creatCommTempl(@RequestBody CommandTemplates commTempl,
			HttpServletResponse httpResponse_p, WebRequest request_p) {
		CommandTemplates commTemp =null;
		logger_c.debug("Create/Modify command Template: " + commTempl.toString());

		try {
			commTemp = commTemplateService.createCommTempl(commTempl, CREATED_USER_ID);
		} catch (Exception e) {
			String sMessage = "Error creating new comm Template. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request_p.getContextPath() + "/rest/createEnv/" + commTempl.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, commTemp);
	}
		
	@RequestMapping(value = "/rest/getAllCommTemplates/", method = RequestMethod.GET)
	public ModelAndView getAllEnvs() {
		List<CommandTemplates> commTemplates = null;

		try {
			commTemplates = commTemplateService.getAllCommandTemplates();
		} catch (Exception e) {
			String sMessage = "Error getting all funds. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing comm Templates: " + commTemplates.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, commTemplates);
	}

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
	
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/deleteCommTempl/", method = RequestMethod.POST)
	public ModelAndView deleteCommTempl(@RequestBody  CommandTemplates commTempl) {
		
		/* validate fund Id parameter */
		if (commTempl == null) {
			String sMessage = "Error invoking deleteCommTempl - Invalid CommandTemplates parameter";
			return createErrorResponse(sMessage);
		}

		try {
			//int envId = Integer.parseInt(id_p);
			commTemplateService.deleteCommTemplById(commTempl);
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error invoking deleteCommTempl. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + commTempl.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, commTempl);
	}
}
