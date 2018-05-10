package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import com.taotao.portal.service.impl.UserServiceImpl;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserServiceImpl userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// 响应用户之后

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// handler执行之后，ModelAndView返回之前

	}

	/**
	 * 从cookie取token
	 * 根据token换取用户信息，调用sso接口
	 * 取不到->跳转到登录页面（用户url传递给callback），返回false
	 * 取到->直接放行，返回true
	 */
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		// Handler执行之前，true执行，false不执行
		
//		String token = CookieUtils.getCookieValue(arg0, "TT_TOKEN");
//		TbUser user = userService.getUserByToken(token);
//		
//		if(user == null){
//			arg1.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect=" + arg0.getRequestURL());
//			//返回false
//			return false;
//
//		}
		return true;
	}

}
