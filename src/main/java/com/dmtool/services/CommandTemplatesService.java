package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.DeploymentOptions;

public interface CommandTemplatesService {
	public List<CommandTemplates> getAllCommandTemplatesByCode(String code);
	public List<CommandTemplates> getAllCommandTemplates();
	public CommandTemplates createCommTempl(CommandTemplates commTempl,
			int createdUserId);
	public void deleteCommTemplById(CommandTemplates commTempl);
	public List<CommandTemplates> getAllCommTemplByUIScreenLocation(
			String string);
}