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

import com.dmtool.domain.CommandParams;
import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.EnvInfo;
import com.dmtool.services.CommandParamsService;
import com.dmtool.services.CommandTemplatesService;
import com.dmtool.services.EnvService;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class CommandParamsController extends RootController{

	@Autowired
	private CommandParamsService commParamService;

	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(CommandParamsController.class);
		
	@RequestMapping(value = "/rest/admin/getAllCommParamsByCode/", method = RequestMethod.POST)
	public ModelAndView getAllCommParamsByCode(@RequestBody CommandParams commandParams,
			HttpServletResponse httpResponse_p, WebRequest request_p) {
		List<CommandParams> commandParamsList = null;

		try {
			commandParamsList = commParamService.getAllCommandParamsByCode(commandParams);
		} catch (Exception e) {
			String sMessage = "Error getting all funds. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing comm Params: " + commandParamsList.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, commandParamsList);
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
}
