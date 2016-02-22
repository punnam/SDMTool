package com.dmtool.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.CommandParams;
import com.dmtool.domain.CommandTemplates;

@Repository
public class CommandParamsDao extends HibernateDaoSupport{

	public List<CommandParams> getAllCommandParamsByCode(String code) {
	       @SuppressWarnings("unchecked")
		List<CommandParams> params =  getHibernateTemplate().find("from CommandParams d where d.code = ? order by d.order", new Object[]{code});
	    return params;
	}

	public CommandParams createCommandDao(CommandParams commParams) {
		// TODO Auto-generated method stub
		return null;
	}	@Override
	public void deleteCommTemplById(CommandParams commParams) {
		commandTemplatesDao.deleteCommTemplById(commTempl);
	}
	
	
}

