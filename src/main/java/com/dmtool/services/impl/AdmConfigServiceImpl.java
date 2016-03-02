package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmtool.dao.impl.AdmConfigDao;
import com.dmtool.domain.AdmConfig;
import com.dmtool.services.AdmConfigService;
@Service
public class AdmConfigServiceImpl  implements AdmConfigService{
	private Logger logger = Logger.getLogger(AdmConfigServiceImpl.class);
	@Autowired
	private AdmConfigDao admConfigDao;
	
	@Override
	public AdmConfig getAdmConfigById(int AdmConfigId) {
		return admConfigDao.getAdmConfigById(AdmConfigId);
	}

	@Override
	public List<AdmConfig> getAllAdmConfig() {
		// TODO Auto-generated method stub
		return admConfigDao.getAllAdmConfig();
	}

	@Override
	public void deleteAdmConfig(AdmConfig admConfig) {
		admConfigDao.deleteAdmConfig(admConfig);
	}

	@Override
	public AdmConfig createAdmConfig(AdmConfig admConfig, int userId) {
		Integer objectId = admConfig.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			admConfig.setCreatedTime(currentTimestamp);
			admConfig.setUpdatedTime(currentTimestamp);
			admConfig.setCreatedUser(userId);
			admConfig.setUpdatedUser(userId);
		}else{
			
			admConfig.setUpdatedTime(currentTimestamp);
			admConfig.setUpdatedUser(userId);
		}
		admConfig = admConfigDao.createAdmConfig(admConfig);
		return admConfig;
	}

	@Override
	public void modifyAdmConfig(AdmConfig admConfig) {
		admConfigDao.modifyAdmConfig(admConfig);
		
	}

	@Override
	public AdmConfig getAdmConfigByEnvName(
			String selectedEnvName) {
		// TODO Auto-generated method stub
		List<AdmConfig> admConfigList = admConfigDao.getAdmConfigByEnvName(selectedEnvName);
		
		if(admConfigList != null && admConfigList.size() > 0){
			return admConfigList.get(0);
		}
		return null;
	}

}
