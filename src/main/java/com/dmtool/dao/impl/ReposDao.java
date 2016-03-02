package com.dmtool.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.Repos;

@Repository
public class ReposDao extends HibernateDaoSupport{
	//private SessionFactory sessionFactory;
	
	public Repos getReposById(int repoId) {
       Repos repos  = (Repos)getHibernateTemplate().load(Repos.class, repoId);
       return repos;
	}

	
	public List<Repos> getAllRepos() {
	       @SuppressWarnings("unchecked")
		List<Repos> reposList = (List<Repos>)getHibernateTemplate().find("from Repos");
	    return reposList;
	}

	
	public void deleteRepos(Repos repos) {
		//Repos envInfo = getReposById(envId);
		getHibernateTemplate().delete(repos);
	}

	
	public Repos createRepos(Repos repos) {
		getHibernateTemplate().saveOrUpdate(repos);
		return repos;
	}


	public void modifyRepos(Repos repos) {
		getHibernateTemplate().save(repos);
		
	}


	public List<Repos> getRepoInfoByEnvNameAndActionType(
			String selectedEnvName) {
		@SuppressWarnings("unchecked")
		List<Repos> repos = getHibernateTemplate().find("from Repos r where r.envName = ?", new Object[]{selectedEnvName});
	    return repos;

	}

}
