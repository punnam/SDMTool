package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.CommandTemplatesDao;
import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.DeploymentOptions;
import com.dmtool.domain.EnvInfo;
import com.dmtool.services.CommandTemplatesService;
@Repository
public class CommandTemplatesServiceImpl implements CommandTemplatesService {
	private Logger logger = Logger.getLogger(CommandTemplatesServiceImpl.class);
	@Autowired
	private CommandTemplatesDao commandTemplatesDao;
	
	@Override
	public List<CommandTemplates> getAllCommandTemplatesByCode(String code) {
		
		return commandTemplatesDao.getAllCommandTemplatesByCode(code);
	}
	@Override
	public List<CommandTemplates> getAllCommandTemplates() {
		
		return commandTemplatesDao.getAllCommandTemplates();
	}
	@Override
	public CommandTemplates createCommTempl(CommandTemplates commTempl,
			int userId) {
		Integer objectId = commTempl.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			commTempl.setCreatedTime(currentTimestamp);
			commTempl.setUpdatedTime(currentTimestamp);
			commTempl.setCreatedUser(userId);
			commTempl.setUpdatedUser(userId);
		}else{
			commTempl.setUpdatedTime(currentTimestamp);
			commTempl.setUpdatedUser(userId);
		}
		commTempl = commandTemplatesDao.createCommandTemplate(commTempl);
		return commTempl;
	
	}
	@Override
	public void deleteCommTemplById(CommandTemplates commTempl) {
		commandTemplatesDao.deleteCommTemplById(commTempl);
	}
	@Override
	public List<CommandTemplates> getAllCommTemplByUIScreenLocation(
			String uiScreenLocation) {
		// TODO Auto-generated method stub
		return commandTemplatesDao.getAllCommTemplByUIScreenLocation(uiScreenLocation);
	}

}
