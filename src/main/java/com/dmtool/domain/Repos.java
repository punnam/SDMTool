package com.dmtool.domain;

public class Repos  extends AuditInfo{
	private Integer id;
	private String envName;
	private String userId;
	private String password;
	private String odbc;
	private String repoType;
	private String repoName;
	private String tableDDLSync;
	private String indexDDLSync;
	private String tableOwner;
	private String tableOwnerPassword;
	public Integer getId() {
		return id;
	}
	public String getTableOwner() {
		return tableOwner;
	}
	public void setTableOwner(String tableOwner) {
		this.tableOwner = tableOwner;
	}
	public String getTableOwnerPassword() {
		return tableOwnerPassword;
	}
	public void setTableOwnerPassword(String tableOwnerPassword) {
		this.tableOwnerPassword = tableOwnerPassword;
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
	public String getOdbc() {
		return odbc;
	}
	public void setOdbc(String odbc) {
		this.odbc = odbc;
	}
	public String getRepoType() {
		return repoType;
	}
	public void setRepoType(String repoType) {
		this.repoType = repoType;
	}
	public String getRepoName() {
		return repoName;
	}
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
	public String getTableDDLSync() {
		return tableDDLSync;
	}
	public void setTableDDLSync(String tableDDLSync) {
		this.tableDDLSync = tableDDLSync;
	}
	public String getIndexDDLSync() {
		return indexDDLSync;
	}
	public void setIndexDDLSync(String indexDDLSync) {
		this.indexDDLSync = indexDDLSync;
	}
}
