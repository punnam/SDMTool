package com.dmtool.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.AdmConfig;
import com.dmtool.domain.Repos;

@Repository
public class AdmConfigDao extends HibernateDaoSupport{
	//private SessionFactory sessionFactory;
	
	public AdmConfig getAdmConfigById(int admConfigId) {
       AdmConfig config  = (AdmConfig)getHibernateTemplate().load(AdmConfig.class, admConfigId);
       return config;
	}

	
	public List<AdmConfig> getAllAdmConfig() {
	       @SuppressWarnings("unchecked")
	       List<AdmConfig> admConfigList =  getHibernateTemplate().find("from AdmConfig");
	    return admConfigList;
	}

	
	public void deleteAdmConfig(AdmConfig admConfig) {
		//Repos envInfo = getReposById(envId);
		getHibernateTemplate().delete(admConfig);
	}
	

	
	public AdmConfig createAdmConfig(AdmConfig admConfig) {
		getHibernateTemplate().saveOrUpdate(admConfig);
		return admConfig;
	}


	public void modifyAdmConfig(AdmConfig admConfig) {
		getHibernateTemplate().save(admConfig);
		
	}


	public List<AdmConfig> getAdmConfigByEnvName(
			String selectedEnvName) {
		List<AdmConfig> admConfigs = getHibernateTemplate().find("from AdmConfig r where r.envName=?", selectedEnvName);
	    return admConfigs;

	}

}
