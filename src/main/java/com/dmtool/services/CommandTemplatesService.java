package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.CommandTemplates;

public interface CommandTemplatesService {
	public List<CommandTemplates> getAllCommandTemplatesByCode(String code);
}