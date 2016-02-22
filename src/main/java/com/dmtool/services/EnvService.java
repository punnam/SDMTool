package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.EnvInfo;

public interface EnvService {

	EnvInfo getEnvInfoById(int envId);

	List<EnvInfo> getAllEnvs();

	void deleteEnvInfoById(EnvInfo env_p);

	EnvInfo createEnv(EnvInfo env_p, int userId);

	void modifyEnv(EnvInfo env_p);
	public List<EnvInfo> getAllEnvNames();

	List<EnvInfo> getAllEnvByEnvName(String envName);
	public EnvInfo getEnvById(Integer id);
	
}
