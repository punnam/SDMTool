package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.CommandParams;

public interface CommandParamsService {
	public List<CommandParams> getAllCommandParamsByCode(String code);
}