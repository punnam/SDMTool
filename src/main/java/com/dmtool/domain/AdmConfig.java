package com.dmtool.domain;

public class AdmConfig  extends AuditInfo{
	private Integer id;
	private String envName;
	private String seibelServer;
	private String sessionId;
	private String admConfigType;
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
	public String getSeibelServer() {
		return seibelServer;
	}
	public void setSeibelServer(String seibelServer) {
		this.seibelServer = seibelServer;
	}

	public String getAdmConfigType() {
		return admConfigType;
	}
	public void setAdmConfigType(String admConfigType) {
		this.admConfigType = admConfigType;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
