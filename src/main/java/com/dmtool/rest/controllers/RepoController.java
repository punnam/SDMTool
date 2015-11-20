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

import com.dmtool.domain.EnvInfo;
import com.dmtool.domain.Repos;
import com.dmtool.services.EnvService;
import com.dmtool.services.ReposService;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class RepoController extends RootController{
	@Autowired
	private EnvService envService;

	@Autowired
	private ReposService repoService;
	
	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(EnvController.class);
	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
	}
	@RequestMapping(value = { "/rest/createRepo/" }, method = { RequestMethod.POST })
	public ModelAndView createRepo(@RequestBody Repos repos,
			HttpServletResponse httpResponse_p, WebRequest request_p) {
		
		logger_c.debug("Creating Env: " + repos.toString());

		try {
			repoService.createRepos(repos, CREATED_USER_ID);
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request_p.getContextPath() + "/rest/createrepos/" + repos.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, repos);
	}
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/getRepos/{id}", method = RequestMethod.GET)
	public ModelAndView getRepos(@PathVariable("id") String id_p) {
		EnvInfo envInfo = null;

		/* validate fund Id parameter */
		if (isEmpty(id_p)) {
			String sMessage = "Error invoking getEnv - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			int envId = Integer.parseInt(id_p);
			envInfo = envService.getEnvInfoById(envId);
		} catch (Exception e) {
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + envInfo.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, envInfo);
	}
	
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/repos/deleteRepo/", method = RequestMethod.POST)
	public ModelAndView deleteRepos(@RequestBody Repos repos) {
		
		/* validate fund Id parameter */
		if (repos == null) {
			String sMessage = "Error invoking getEnv - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			//int envId = Integer.parseInt(id_p);
			repoService.deleteRepos(repos);
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + repos.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, repos);
	}
	/**
	 * Gets a fund by fund id.
	 *
	 * @param fundId_p
	 *            the fund id_p
	 * @return the fund
	 */
	@RequestMapping(value = "/rest/modifyRepos/", method = RequestMethod.POST)
	public ModelAndView modifyRepos(@RequestBody Repos repos) {
		
		/* validate fund Id parameter */
		if (repos == null) {
			String sMessage = "Error invoking modifyRepos - Invalid env Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			//int envId = Integer.parseInt(id_p);
			repoService.modifyRepos(repos);
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error invoking getFund. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Fund: " + repos.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, repos);
	}
		
	@RequestMapping(value = "/rest/getAllRepos/", method = RequestMethod.GET)
	public ModelAndView getAllRepos() {
		List<Repos> repos = null;

		try {
			repos = repoService.getAllRepos();
		} catch (Exception e) {
			String sMessage = "Error getting all funds. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing Persons: " + repos.toString());
		return new ModelAndView(jsonView_i, DATA_FIELD, repos);
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
