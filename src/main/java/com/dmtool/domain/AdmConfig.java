package com.dmtool.domain;

public class AdmConfig  extends AuditInfo{
	private Integer id;
	private String envName;
	private String userId;
	private String password;
	private String seibelServer;
	private String rowId;
	private String logFilePath;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSeibelServer() {
		return seibelServer;
	}
	public void setSeibelServer(String seibelServer) {
		this.seibelServer = seibelServer;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	public String getAdmConfigType() {
		return admConfigType;
	}
	public void setAdmConfigType(String admConfigType) {
		this.admConfigType = admConfigType;
	}
}
