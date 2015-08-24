package com.vinniccius.interceptor;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;

@Intercepts
public class CORSInterceptor {

	private ServletContext servletContext;
	private HttpServletResponse response;
	
	protected CORSInterceptor() {
		this(null, null);
	}

	@Inject
	public CORSInterceptor(ServletContext servletContext, HttpServletResponse response) {
		this.servletContext = servletContext;
		this.response = response;
	}

	@BeforeCall
	public void intercept() throws InterceptionException {
		String enabled = initParam("CORS.enabled");
		if (Boolean.valueOf(enabled)) {
			addHeader("Access-Control-Allow-Origin");
			addHeader("Access-Control-Allow-Credentials");
			addHeader("Access-Control-Expose-Headers");
		}
	}

	private void addHeader(String header) {
		String param = initParam(header);
		if (!Strings.isNullOrEmpty(param)) {
			response.addHeader(header, param);
		}
	}

	private String initParam(String name) {
		return servletContext.getInitParameter(name);
	}
}