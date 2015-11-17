package com.dmtool.domain;

public class AgentInfo {	
	private int id;
	private String host;
	private int remotePort;
	private String remoteUser;
	private String remoteAddress;
	private String serverName;
	int serverPort;
	public AgentInfo(){
		
	}
	public AgentInfo(String host, int remotePort, String remoteUser, String remoteAddress, String serverName,
			int serverPort) {
		super();
		this.host = host;
		this.remotePort = remotePort;
		this.remoteUser = remoteUser;
		this.remoteAddress = remoteAddress;
		this.serverName = serverName;
		this.serverPort = serverPort;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	public String getRemoteUser() {
		return remoteUser;
	}
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
