package com.dmtool.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dmtool.domain.CommandTemplates;
import com.dmtool.domain.DeploymentOptions;
import com.dmtool.domain.EnvInfo;

@Repository
public class CommandTemplatesDao extends HibernateDaoSupport{

	public List<CommandTemplates> getAllCommandTemplatesByCode(String code) {
	       @SuppressWarnings("unchecked")
		List<CommandTemplates> commands =  getHibernateTemplate().find("from CommandTemplates d where d.code = ? order by d.commandOrder", new Object[]{code});
	    return commands;
	}
	public List<CommandTemplates> getAllCommandTemplates() {
	       @SuppressWarnings("unchecked")
		List<CommandTemplates> commands =  getHibernateTemplate().find("from CommandTemplates");
	    return commands;
	}
	
	public CommandTemplates createCommandTemplate(CommandTemplates commandTemplate) {
		getHibernateTemplate().saveOrUpdate(commandTemplate);
		return commandTemplate;
	}
	public void deleteCommTemplById(CommandTemplates commTempl) {
		getHibernateTemplate().delete(commTempl);
		
	}
	public List<CommandTemplates> getAllCommTemplByUIScreenLocation(String uiScreenLocation) {
	       @SuppressWarnings("unchecked")
		List<CommandTemplates> commandTemplatesList = (List<CommandTemplates>)getHibernateTemplate().find("from CommandTemplates d where d.uiScreenLocation=?", uiScreenLocation);
	    return commandTemplatesList;
	}
}

