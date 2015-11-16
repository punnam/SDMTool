package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmtool.dao.impl.EnvDao;
import com.dmtool.domain.EnvInfo;
import com.dmtool.services.EnvService;

@Service
public class EnvServiceImpl implements EnvService{
	@Autowired
	private EnvDao envDao;
	
	@Override
	public EnvInfo getEnvInfoById(int envId) {
		EnvInfo envInfo = envDao.getEnvInfoById(envId);
		return envInfo;
	}

	@Override
	public List<EnvInfo> getAllEnvs() {
		List<EnvInfo> envInfos = envDao.getAllEnvs();
		return envInfos;
	}

	@Override
	public void deleteEnvInfoById(EnvInfo env_p) {
		 envDao.deleteEnvInfoById(env_p);
	}

	@Override
	public EnvInfo createEnv(EnvInfo envInfo) {
		Integer objectId = envInfo.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			envInfo.setCreateTime(currentTimestamp);
			envInfo.setUpdateTime(currentTimestamp);
			envInfo.setCreateUser(new Integer(0000));
			envInfo.setUpdateUser(new Integer(0000));
		}else{
			
			envInfo.setUpdateTime(currentTimestamp);
			envInfo.setUpdateUser(new Integer(1111));
		}
		envInfo = envDao.createEnv(envInfo);
		return envInfo;
	}

	public EnvDao getEnvDao() {
		return envDao;
	}

	public void setEnvDao(EnvDao envDao) {
		this.envDao = envDao;
	}

	@Override
	public void modifyEnv(EnvInfo env_p) {
		 envDao.modifyEnv(env_p);
		
	}

	@Override
	public List<EnvInfo> getAllEnvNames() {
		// TODO Auto-generated method stub
		return envDao.getAllEnvNames();
	}

	@Override
	public List<EnvInfo> getAllEnvByEnvName(String envName) {
		// TODO Auto-generated method stub
		return envDao.getAllEnvByEnvName(envName);
	}
}
