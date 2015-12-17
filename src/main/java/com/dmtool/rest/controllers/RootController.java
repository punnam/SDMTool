package com.dmtool.rest.controllers;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dmtool.domain.UserInfo;

public class RootController {
	public static final int CREATED_USER_ID = 0000;
	public static final int UPDATED_USER_ID = 1111;
	public boolean validateUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		UserInfo userInSession = (UserInfo) session.getAttribute("");
		UserInfo userInRequest =null;
		String currentSessionId = session.getId();
		
		Enumeration<String> headersEnum = request.getHeaderNames();
		
		String userNameFromReq = "";
		String sessionIdFromReq = "";

		while (headersEnum.hasMoreElements()) {
			String headerName = (String) headersEnum.nextElement();
			if(headerName.equals("USER_NAME")){
				userNameFromReq = request.getHeader("USER_NAME");
			}
			if(headerName.equals("SESSION_ID")){
				sessionIdFromReq = request.getHeader("SESSION_ID");
			}	
		}
		String sessionIdInReq = (String) request.getAttribute("");
		if(!userInRequest.getUserId().equals(userInSession.getUserId())){
			session.invalidate();
			return false;
		}
		if(!currentSessionId.equals(sessionIdInReq)){
			session.invalidate();
			return false;
		}
		return true;
	}
}
