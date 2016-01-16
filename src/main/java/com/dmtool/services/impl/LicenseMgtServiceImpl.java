package com.dmtool.services.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmtool.dao.impl.LicenseMgtDao;
import com.dmtool.services.LicenseMgtService;

@Service
public class LicenseMgtServiceImpl implements LicenseMgtService {
	private static final Logger logger = Logger.getLogger(LicenseMgtServiceImpl.class);

	@Autowired
	private LicenseMgtDao licenseMgtDao;
	
}
