package com.dmtool.user.actions;

import java.util.List;

public class DeploymentOptionsActions {
	private Integer id;
	private String envName;
	private List<String> deploymentServices;
	private String actionType;

	public List<String> getDeploymentServices() {
		return deploymentServices;
	}
	public void setDeploymentServices(List<String> deploymentServices) {
		this.deploymentServices = deploymentServices;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
}
