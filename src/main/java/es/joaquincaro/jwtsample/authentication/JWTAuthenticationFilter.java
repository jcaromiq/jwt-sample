package es.joaquincaro.jwtsample.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private TokenAuthenticationService tokenAuthenticationService;
	
	private static final String HEADER_STRING = "Authorization";

	public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
		super();
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		String token = ((HttpServletRequest) request).getHeader(HEADER_STRING);
		Authentication authentication = tokenAuthenticationService.getAuthentication(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

}
