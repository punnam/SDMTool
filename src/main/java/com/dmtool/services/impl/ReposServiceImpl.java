package com.dmtool.services.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.ReposDao;
import com.dmtool.domain.Repos;
import com.dmtool.services.ReposService;

@Repository
public class ReposServiceImpl implements ReposService{
	private static final Logger logger = Logger.getLogger(ReposServiceImpl.class);
	
	@Autowired
	private ReposDao reposDao;
	
	@Override
	public Repos getReposById(int repoId) {
		return reposDao.getReposById(repoId);
	}

	@Override
	public List<Repos> getAllRepos() {
		// TODO Auto-generated method stub
		return reposDao.getAllRepos();
	}

	@Override
	public void deleteRepos(Repos repo) {
		reposDao.deleteRepos(repo);
	}

	@Override
	public Repos createRepos(Repos repos, int userId) {
		Integer objectId = repos.getId();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		if(objectId == null){
			repos.setCreatedTime(currentTimestamp);
			repos.setUpdatedTime(currentTimestamp);
			repos.setCreatedUser(userId);
			repos.setUpdatedUser(userId);
		}else{
			repos.setUpdatedTime(currentTimestamp);
			repos.setUpdatedUser(userId);
		}
		repos = reposDao.createRepos(repos);
		return repos;
		
		
	}

	@Override
	public void modifyRepos(Repos repos) {
		reposDao.modifyRepos(repos);
		
	}

	@Override
	public Repos getRepoInfoByEnvNameAndActionType(
			String selectedEnvName, String actionType) {
		// TODO Auto-generated method stub
		List<Repos> reposList = reposDao.getRepoInfoByEnvNameAndActionType(
				selectedEnvName, actionType);
		if(reposList != null && reposList.size() > 0){
			return reposList.get(0);
		}
		return null;
	}

}
