package com.dmtool.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.ConfirmUserPass;
import com.dmtool.domain.UserInfo;

@Repository
public class UserInfoDao extends HibernateDaoSupport {

	public List<UserInfo> getUserByUserId(String userId) {
		@SuppressWarnings("unchecked")
		List<UserInfo> users = getHibernateTemplate().find(
				"from UserInfo d where d.userId=?", new Object[]{userId});
		return users;
	}

	public UserInfo createUserInfo(UserInfo userInfo) {

		getHibernateTemplate().saveOrUpdate(userInfo);

		return userInfo;
	}

	public UserInfo getUserInfoByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
}