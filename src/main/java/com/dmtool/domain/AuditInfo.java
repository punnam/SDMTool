package com.dmtool.domain;

import java.sql.Timestamp;

public class AuditInfo {
	private Timestamp createdTime;
	private Timestamp updatedTime;
	private Integer createdUser;
	private Integer updatedUser;
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Integer getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(Integer createdUser) {
		this.createdUser = createdUser;
	}
	public Integer getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(Integer updatedUser) {
		this.updatedUser = updatedUser;
	}
}
