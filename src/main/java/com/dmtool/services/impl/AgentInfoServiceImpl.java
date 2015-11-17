package com.dmtool.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.AgentInfoDao;
import com.dmtool.domain.AgentInfo;
import com.dmtool.services.AgentInfoService;
@Repository
public class AgentInfoServiceImpl implements AgentInfoService {

	@Autowired
	AgentInfoDao agentInfoDao;

	@Override
	public AgentInfo createAgentInfo(AgentInfo agentInfo) {
		return agentInfoDao.createUserInfo(agentInfo);
	}
}