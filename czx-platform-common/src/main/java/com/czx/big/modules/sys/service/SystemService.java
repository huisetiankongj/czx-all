package com.czx.big.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czx.big.common.service.BaseService;
import com.czx.big.modules.sys.dao.UserDao;
import com.czx.big.modules.sys.entity.User;

@Service
public class SystemService extends BaseService{

	@Autowired
	private UserDao userDao;
	
	public User getUser(String id) {
		return userDao.get(id);
	}
	
}
