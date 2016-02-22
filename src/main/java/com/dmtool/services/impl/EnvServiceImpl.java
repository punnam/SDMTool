package com.dmtool.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmtool.dao.impl.EnvDao;
import com.dmtool.domain.EnvInfo;
import com.dmtool.services.EnvService;

@Service
public class EnvServiceImpl implements EnvService{
	private static final Logger logger = Logger.getLogger(EnvServiceImpl.class);

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
	public EnvInfo createEnv(EnvInfo envInfo, int userId) {
		Integer objectId = envInfo.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			envInfo.setCreatedTime(currentTimestamp);
			envInfo.setUpdatedTime(currentTimestamp);
			envInfo.setCreatedUser(userId);
			envInfo.setUpdatedUser(userId);
		}else{
			
			envInfo.setUpdatedTime(currentTimestamp);
			envInfo.setUpdatedUser(userId);
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
		List<EnvInfo> envInfoList =  envDao.getAllEnvNames();
		List<EnvInfo> envInfoUniqueList =  new ArrayList<EnvInfo>();
		List<String> envNamesTemp = new ArrayList<String>();
		EnvInfo envInfoFroSelectEnv = new EnvInfo();
		envInfoFroSelectEnv.setName("");
		envInfoFroSelectEnv.setDesc("Select Environment");
		envInfoUniqueList.add(envInfoFroSelectEnv);
		for (Iterator<EnvInfo> iterator = envInfoList.iterator(); iterator.hasNext();) {
			EnvInfo envInfo = iterator.next();
			if(envNamesTemp.contains(envInfo.getName())){
				//
			}else{
				envNamesTemp.add(envInfo.getName());
				envInfoUniqueList.add(envInfo);
			}
		}
		return envInfoUniqueList;
	}

	@Override
	public List<EnvInfo> getAllEnvByEnvName(String envName) {
		// TODO Auto-generated method stub
		return envDao.getAllEnvByEnvName(envName);
	}
	@Override
	public EnvInfo getEnvById(Integer id) {
		// TODO Auto-generated method stub
		return envDao.getEnvById(id);
	}
}
