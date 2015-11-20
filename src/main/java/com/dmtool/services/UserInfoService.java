package com.dmtool.services;

import com.dmtool.domain.UserInfo;

public interface UserInfoService {
	public UserInfo getUserByUserId(String userId);

	public boolean logIn(UserInfo userInfo);	
	public UserInfo createUserInfo(UserInfo userInfo, int userId);

	public UserInfo getUserInfo(UserInfo userInfo);
}
