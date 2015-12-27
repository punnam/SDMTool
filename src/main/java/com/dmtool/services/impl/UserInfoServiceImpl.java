package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.UserInfoDao;
import com.dmtool.domain.ConfirmUserPass;
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
		if(userInfoFromDB != null && userInfo != null){
			if(userInfoFromDB.getPassword().equals(userInfo.getPassword())){
				return userInfoFromDB;
			}
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

	@Override
	public boolean resetPassword(ConfirmUserPass userInfo, int createdUserId) {
		if (userInfo != null
				&& userInfo.getPassword().equals(userInfo.getConfirmPassword())) {
			UserInfo userInfofromDB = getUserByUserId(userInfo.getUserId());
			if (userInfofromDB != null) {
				userInfofromDB.setPassword(userInfo.getPassword());
				createUserInfo(userInfofromDB, createdUserId);
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}
}
