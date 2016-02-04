package com.dmtool.services;

import java.util.HashMap;
import java.util.List;

import com.dmtool.domain.DeploymentOptions;
import com.dmtool.user.actions.DeploymentOptionsActions;

public interface DeploymentOptionsService {
	List<DeploymentOptions> getAllDeploymentOptions();
	List<DeploymentOptions> getAllDeploymentPackages();
	HashMap<String, HashMap<String, List<String>>> processdeDloymentOptionsService(DeploymentOptionsActions env_p);
}
