package com.dmtool.services;

import com.dmtool.domain.ConfirmUserPass;
import com.dmtool.domain.UserInfo;

public interface UserInfoService {
	public UserInfo getUserByUserId(String userId);

	public UserInfo logIn(UserInfo userInfo);	
	public UserInfo createUserInfo(UserInfo userInfo, int userId);

	public UserInfo getUserInfo(UserInfo userInfo);

	public boolean resetPassword(ConfirmUserPass userInfo, int createdUserId);
}
