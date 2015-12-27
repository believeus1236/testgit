package cn.believeus.shiro;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import cn.believeus.model.Admin;
import cn.believeus.service.IService;
import cn.believeus.variables.Variables;


public class AuthenticationFilter extends FormAuthenticationFilter {
	
	@Resource
	private IService mysqlService;
	public AuthenticationFilter() {
	}

	// 判断请求是否被拒绝
	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse response) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		return super.onAccessDenied(request, response);
	}

	// 提交表单的时候如果没有被拒绝创建调用
	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		String username=httpRequest.getParameter("username");
		String password=httpRequest.getParameter("password");
		return new TokenAuthentication(username, password,true);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		Session session = subject.getSession();
		TokenAuthentication authenticationToken = (TokenAuthentication) token;
		String username = authenticationToken.getUsername();
		Admin sessionUser = (Admin)mysqlService.findObject(Admin.class, Variables.USER_NAME, username);
		session.setAttribute(Variables.SESSION_USER,sessionUser);
		return super.onLoginSuccess(token, subject, servletRequest, servletResponse);
	}
}
