package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.CommandParamsDao;
import com.dmtool.domain.CommandParams;
import com.dmtool.domain.CommandTemplates;
import com.dmtool.services.CommandParamsService;
@Repository
public class CommandParamsServiceImpl implements CommandParamsService {
	private Logger logger = Logger.getLogger(CommandParamsServiceImpl.class);
	@Autowired
	private CommandParamsDao commandParamsDao;
	
	@Override
	public List<CommandParams> getAllCommandParamsByCode(CommandParams commandParams) {
		
		return commandParamsDao.getAllCommandParamsByCode(commandParams.getCode());
	}
	@Override
	public CommandParams createCommParams(CommandParams commParams,
			int userId) {
		Integer objectId = commParams.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			commParams.setCreatedTime(currentTimestamp);
			commParams.setUpdatedTime(currentTimestamp);
			commParams.setCreatedUser(userId);
			commParams.setUpdatedUser(userId);
		}else{
			commParams.setUpdatedTime(currentTimestamp);
			commParams.setUpdatedUser(userId);
		}
		commParams = commandParamsDao.createCommandDao(commParams);
		return commParams;
	
	}
	@Override
	public void deleteCommParamById(CommandParams commParams) {
		commandParamsDao.deleteCommParamById(commTempl);
	}
}
