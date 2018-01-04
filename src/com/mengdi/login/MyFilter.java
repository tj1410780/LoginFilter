package com.mengdi.login;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 * Servlet Filter implementation class MyFilter
 */
@WebFilter(filterName="MyFilter", value={"/*"}, dispatcherTypes={DispatcherType.REQUEST}, initParams={
		@WebInitParam(name="noLoginPaths", value="login.jsp;fail.jsp;LoginServlet"),
		@WebInitParam(name="charSet", value="UTF-8")
})
public class MyFilter implements Filter {
	private FilterConfig config;
    /**
     * Default constructor. 
     */
    public MyFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		
		
		//编码转换
		String charSet = config.getInitParameter("charSet");
		if (charSet == null) {
			charSet = "UTF-8";
		} 
		req.setCharacterEncoding(charSet);
		
		//直接放行的3个URL
		String noLoginPaths = config.getInitParameter("noLoginPaths");		
		if (noLoginPaths != null) {
			String[] strArray = noLoginPaths.split(";");
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i] == null || "".equals(strArray[i]))
					continue;
				if (req.getRequestURI().indexOf(strArray[i]) != -1) {
					System.out.println(req.getRequestURI().indexOf(strArray[i]));
					chain.doFilter(req, res);
					return;
				}
			}
		} 
		
		
		//注意啊，这里是if,不是else if！！！！
		if (session.getAttribute("userName")!=null) {
				chain.doFilter(req, res);
		} else {
			res.sendRedirect(req.getContextPath() + "/login.jsp");
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		config = fConfig;
	}

}
