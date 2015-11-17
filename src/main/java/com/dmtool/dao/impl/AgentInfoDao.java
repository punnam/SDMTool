package com.dmtool.dao.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.AgentInfo;

@Repository
public class AgentInfoDao extends HibernateDaoSupport {

	public AgentInfo createUserInfo(AgentInfo agentInfo) {

		getHibernateTemplate().saveOrUpdate(agentInfo);

		return agentInfo;
	}
}