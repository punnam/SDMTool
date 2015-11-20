package com.dmtool.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.EnvInfo;

@Repository
public class EnvDao extends HibernateDaoSupport{
	//private SessionFactory sessionFactory;
	
	public EnvInfo getEnvInfoById(int envId) {
       EnvInfo envInfo  = (EnvInfo)getHibernateTemplate().load(EnvInfo.class, envId);
       return envInfo;
	}

	
	public List<EnvInfo> getAllEnvs() {
	       @SuppressWarnings("unchecked")
		List<EnvInfo> envInfoList = (List<EnvInfo>)getHibernateTemplate().find("from EnvInfo");
	    return envInfoList;
	}
	public List<EnvInfo> getAllEnvNames() {
	       @SuppressWarnings("unchecked")
		List<EnvInfo> envInfoList = (List<EnvInfo>)getHibernateTemplate().find("from EnvInfo");
	    return envInfoList;
	}
	
	public List<EnvInfo> getAllEnvByEnvName(String name) {
	       @SuppressWarnings("unchecked")
		List<EnvInfo> envInfoList = (List<EnvInfo>)getHibernateTemplate().find("from EnvInfo e where e.name=?", name);
	    return envInfoList;
	}
	
	public void deleteEnvInfoById(EnvInfo env_p) {
		//EnvInfo envInfo = getEnvInfoById(envId);
		getHibernateTemplate().delete(env_p);
	}

	
	public EnvInfo createEnv(EnvInfo envInfo) {
	
			getHibernateTemplate().saveOrUpdate(envInfo);
	
		return envInfo;
	}


	public void modifyEnv(EnvInfo envInfo) {
		getHibernateTemplate().save(envInfo);
		
	}


	public List<EnvInfo> getAllEnvInfoForEnvName(String envName) {
		// TODO Auto-generated method stub
		return null;
	}

}
