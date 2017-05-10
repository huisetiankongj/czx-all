package com.czx.big.modules.sys.dao;

import com.czx.big.common.persistence.CrudDao;
import com.czx.big.common.persistence.annotation.MyBatisDao;
import com.czx.big.modules.sys.entity.User;

@MyBatisDao
public interface UserDao extends CrudDao<User> {

}
