package com.dmtool.services.impl;

import java.util.Calendar;
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
			userInfo = userInfos.get(0);
		}
		return userInfo;
	}

	@Override
	public UserInfo logIn(UserInfo userInfo) {
		UserInfo userInfoFromDB = getUserByUserId(userInfo.getUserId());
		boolean success = false;
		if(userInfoFromDB.getPassword().equals(userInfo.getPassword())){
			success = true;
			return userInfoFromDB;
		}
		return null;
		
	}

	@Override
	public UserInfo createUserInfo(UserInfo userInfo, int userId) {
		Integer objectId = userInfo.getId();
		
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			userInfo.setCreatedTime(currentTimestamp);
			userInfo.setUpdatedTime(currentTimestamp);
			userInfo.setCreatedUser(userId);
			userInfo.setUpdatedUser(userId);
		}else{
			userInfo.setUpdatedTime(currentTimestamp);
			userInfo.setUpdatedUser(userId);
		}
		userInfo = userInfoDao.createUserInfo(userInfo);
		return userInfo;
	}

	@Override
	public UserInfo getUserInfo(UserInfo userInfo) {
		
		return userInfoDao.getUserInfoByUserId(userInfo.getUserId());
		
	}
}
