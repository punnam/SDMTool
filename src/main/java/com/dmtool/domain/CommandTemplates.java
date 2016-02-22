package com.dmtool.domain;

public class CommandTemplates  extends AuditInfo{
	private Integer id;
	private String code;
	private String description;
	private int commandOrder;
	private String command;
	private String uiScreenLocation;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCommand() {
		return command;
	}
	public int getCommandOrder() {
		return commandOrder;
	}
	public void setCommandOrder(int commandOrder) {
		this.commandOrder = commandOrder;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getUiScreenLocation() {
		return uiScreenLocation;
	}
	public void setUiScreenLocation(String uiScreenLocation) {
		this.uiScreenLocation = uiScreenLocation;
	}


}
