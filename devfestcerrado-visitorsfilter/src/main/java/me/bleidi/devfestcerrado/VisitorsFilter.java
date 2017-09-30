package me.bleidi.devfestcerrado;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName="visitorsFilter", urlPatterns="/api/*")
public class VisitorsFilter implements Filter{

	private static final Logger log = Logger.getLogger(VisitorsFilter.class.getCanonicalName());
	private VisitorsDao dao;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.info("init");
		dao = VisitorsDao.get();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("doFilter");
		if(request instanceof HttpServletRequest) {			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			dao.create(new Visitor(request.getRemoteAddr(), httpRequest.getHeader("user-agent")));
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		log.info("destroy");		
	}
}
