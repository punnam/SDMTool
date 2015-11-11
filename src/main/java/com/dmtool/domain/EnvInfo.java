package com.dmtool.domain;

public class EnvInfo {
	private Integer id;
	private String name;
	private String hostName;
	private String enterpriseName;
	private String serverHost;
	private String serverName;
	private String service;
	private String seibelPath;
	private String admPath;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getSeibelPath() {
		return seibelPath;
	}
	public void setSeibelPath(String seibelPath) {
		this.seibelPath = seibelPath;
	}
	public String getAdmPath() {
		return admPath;
	}
	public void setAdmPath(String admPath) {
		this.admPath = admPath;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
}
