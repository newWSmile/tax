package cn.itcast.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.itcast.core.constant.Constant;
import cn.itcast.core.permission.PermissionCheck;
import cn.itcast.nsfw.user.entity.User;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request =(HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();
		//判断当前请求是否是登录的请求
		if (!uri.contains("sys/login_")) {
			if (request.getSession().getAttribute(Constant.USER)!=null) {
				//说明已经登陆过
				//判断是否访问纳税服务系统
				if (uri.contains("/nsfw/")) {
					//访问纳税服务子系统
					User user = (User)request.getSession().getAttribute(Constant.USER);
					//获取spring容器
					WebApplicationContext applicationContext =	WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
					System.out.println("111111111111111111111111:"+applicationContext);
					PermissionCheck pc = (PermissionCheck) applicationContext.getBean("permissionCheck");
					if (pc.isAccessiable(user,"nsfw")) {
						//说明有权限，放行
						chain.doFilter(request, response);
					}else{
						//没有权限，跳转到权限提示页面
						response.sendRedirect(request.getContextPath()+"/sys/login_toNoPermissionUI.action");
					}
				}else{
					//非访问纳税服务子系统，则直接放行
					chain.doFilter(request, response);
				}
				
			}else{
				//没有登录,跳转到登录页面
				response.sendRedirect(request.getContextPath()+"/sys/login_toLoginUI.action");
			}
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
