package com.dmtool.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.UserInfoDao;
import com.dmtool.domain.UserInfo;
import com.dmtool.services.UserInfoService;
@Repository
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	UserInfoDao userInfoDao;
	@Override
	public UserInfo getUserByUserId(String userId) {
		UserInfo userInfo = null;
		List<UserInfo> userInfos = userInfoDao.getUserByUserId(userId);
		if(userInfos != null && userInfos.size() > 0 ){
			userInfo = userInfos.get(1);
		}
		return userInfo;
	}

	@Override
	public boolean logIn(UserInfo userInfo) {
		UserInfo userInfoFromDB = getUserByUserId(userInfo.getUserId());
		boolean success = false;
		if(userInfoFromDB.getPassword().equals(userInfo.getPassword())){
			success = true;
		}
		return success;
	}

	@Override
	public UserInfo createUserInfo(UserInfo userInfo) {
		return userInfoDao.createUserInfo(userInfo);
	}

	@Override
	public UserInfo getUserInfo(UserInfo userInfo) {
		
		return userInfoDao.getUserInfoByUserId(userInfo.getUserId());
		
	}
}
