package com.dmtool.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmtool.dao.impl.CommandParamsDao;
import com.dmtool.domain.CommandParams;
import com.dmtool.services.CommandParamsService;
@Repository
public class CommandParamsServiceImpl implements CommandParamsService {
	private Logger logger = Logger.getLogger(CommandParamsServiceImpl.class);
	@Autowired
	private CommandParamsDao commandParamsDao;
	
	@Override
	public List<CommandParams> getAllCommandParamsByCode(String code) {
		
		return commandParamsDao.getAllCommandParamsByCode(code);
	}

}
