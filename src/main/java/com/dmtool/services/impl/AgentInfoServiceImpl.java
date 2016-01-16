package com.dmtool.services.impl;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.AgentInfoDao;
import com.dmtool.domain.AgentInfo;
import com.dmtool.services.AgentInfoService;
@Repository
public class AgentInfoServiceImpl implements AgentInfoService {
	private Logger logger = Logger.getLogger(AgentInfoServiceImpl.class);
	@Autowired
	AgentInfoDao agentInfoDao;

	@Override
	public AgentInfo createAgentInfo(AgentInfo agentInfo, int userId) {
		Integer objectId = agentInfo.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			agentInfo.setCreatedTime(currentTimestamp);
			agentInfo.setUpdatedTime(currentTimestamp);
			agentInfo.setCreatedUser(userId);
			agentInfo.setUpdatedUser(userId);
		}else{
			
			agentInfo.setUpdatedTime(currentTimestamp);
			agentInfo.setUpdatedUser(userId);
		}
		agentInfo = agentInfoDao.createUserInfo(agentInfo);
		return agentInfo;
		
	}
}