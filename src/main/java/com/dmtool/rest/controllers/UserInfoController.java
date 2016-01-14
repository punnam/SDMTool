package com.dmtool.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.dmtool.domain.ConfirmUserPass;
import com.dmtool.domain.UserInfo;
import com.dmtool.services.UserInfoService;

/**
 * FundsController class will expose a series of RESTful endpoints
 */
@Controller
public class UserInfoController extends RootController{
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(UserInfoController.class);
	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
	}
	@RequestMapping(value = { "/rest/UserInfo/logIn/" }, method = { RequestMethod.POST })
	public ModelAndView logIn(@RequestBody UserInfo userInfo,
			HttpServletResponse httpResponse_p, HttpServletRequest httpRequest) {
		
		logger_c.debug("Creating User Info: " + userInfo.toString());
		boolean logInStatus =  false;
		try {
			UserInfo userInfoFromSystem = userInfoService.logIn(userInfo);
			HttpSession session = httpRequest.getSession();
			if(userInfoFromSystem != null){
				session.setAttribute("CURRENT_USER", userInfoFromSystem);
				logInStatus = true;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			String sMessage = "Error creating new Env. [%1$s]";
			//return createErrorResponse(String.format(sMessage, e.toString()));
			logInStatus = false;
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", httpRequest.getContextPath() + "/rest/userInfo/logIn" + userInfo.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, logInStatus);
	}

	@RequestMapping(value = { "/rest/UserInfo/registerUser/" }, method = { RequestMethod.POST })
	public ModelAndView registerUser(@RequestBody UserInfo userInfo,
			HttpServletResponse httpResponse_p, HttpServletRequest request) {
		
		logger_c.debug("Creating User Info: " + userInfo.toString());

		try {
			//boolean valid = validateUser(request);
			userInfoService.createUserInfo(userInfo, CREATED_USER_ID);
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request.getContextPath() + "/rest/userInfo/logIn" + userInfo.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, "true");
	}

	
	@RequestMapping(value = { "/rest/UserInfo/getUserProfile/" }, method = { RequestMethod.POST })
	public ModelAndView getUserProfile(@RequestBody UserInfo userInfo,
			HttpServletResponse httpResponse_p, HttpServletRequest request) {
		
		logger_c.debug("getUserProfile: " + userInfo.toString());

		try {
			boolean valid = validateUser(request);
			userInfoService.getUserInfo(userInfo);
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request.getContextPath() + "/rest/UserInfo/getUserProfile/" + userInfo.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, userInfo);
	}

	@RequestMapping(value = { "/rest/UserInfo/updateUser/" }, method = { RequestMethod.POST })
	public ModelAndView updateUser(@RequestBody UserInfo userInfo,
			HttpServletResponse httpResponse_p, HttpServletRequest request) {

		logger_c.debug("Creating User Info: " + userInfo.toString());

		try {
			boolean valid = validateUser(request);
			userInfoService.createUserInfo(userInfo, CREATED_USER_ID);
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request.getContextPath() + "/rest/userInfo/logIn" + userInfo.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, userInfo);
	}
	@RequestMapping(value = { "/rest/UserInfo/resetPassword/" }, method = { RequestMethod.POST })
	public ModelAndView resetPassword(@RequestBody ConfirmUserPass userInfo,
			HttpServletResponse httpResponse_p, HttpServletRequest request) {

		logger_c.debug("Reset Password: " + userInfo.toString());
		boolean result = false;

		try {
			//boolean valid = validateUser(request);
			result = userInfoService.resetPassword(userInfo, CREATED_USER_ID);
			
		} catch (Exception e) {
			String sMessage = "Error creating new Env. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		/* set HTTP response code */
		httpResponse_p.setStatus(HttpStatus.CREATED.value());

		/* set location of created resource */
		httpResponse_p.setHeader("Location", request.getContextPath() + "/rest/userInfo/resetPassword" + userInfo.getId());

		/**
		 * Return the view
		 */
		return new ModelAndView(jsonView_i, DATA_FIELD, result);
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