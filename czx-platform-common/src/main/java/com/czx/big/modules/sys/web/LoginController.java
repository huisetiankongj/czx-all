package com.czx.big.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.czx.big.common.web.BaseController;
import com.czx.big.modules.sys.entity.User;
import com.czx.big.modules.sys.service.SystemService;

@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SystemService systemService;
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public Object login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String id = "1";
		User user = systemService.getUser(id);
		return "modules/sys/sysLogin";
	}

}
