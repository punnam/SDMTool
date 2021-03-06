package com.dmtool.services;

import java.util.List;

import com.dmtool.domain.CommandParams;

public interface CommandParamsService {
	public List<CommandParams> getAllCommandParamsByCode(CommandParams commandParams);

	CommandParams createCommParams(CommandParams commParams, int userId);
	public void deleteCommParamById(CommandParams commParams);
}