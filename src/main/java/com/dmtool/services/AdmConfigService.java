package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.AdmConfig;

public interface AdmConfigService {
	public AdmConfig getAdmConfigById(int AdmConfigId);
	public List<AdmConfig> getAllAdmConfig();
	public void deleteAdmConfig(AdmConfig admConfig);
	public AdmConfig createAdmConfig(AdmConfig admConfig, int userId);
	public void modifyAdmConfig(AdmConfig admConfig);
	public AdmConfig getAdmConfigByEnvName(String selectedEnvName);
}
