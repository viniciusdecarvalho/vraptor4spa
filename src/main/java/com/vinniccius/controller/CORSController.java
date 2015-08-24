package com.vinniccius.controller;

import static br.com.caelum.vraptor.view.Results.status;

import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Options;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.http.route.Router;

@Controller
public class CORSController {

	private Result result;
	private Router router;
	private HttpServletRequest request;

	protected CORSController() {
		this(null, null, null);
	}
	
	@Inject
	public CORSController(Result result, Router router, HttpServletRequest request) {
		this.result = result;
		this.router = router;
		this.request = request;
	}
	
	@Options("/*")
	public void options() {
		
		final Set<HttpMethod> allowed = router.allowedMethodsFor(request.getRequestURI());
		final String allowMethods = allowed.toString().replaceAll("\\[|\\]", "");
		result.use(status()).header("Allow", allowMethods);
		result.use(status()).header("Access-Control-Allow-Methods", allowMethods);
		result.use(status()).header("Access-Control-Allow-Headers", "Content-Type, accept, Authorization, origin, X-Requested-With");
		result.use(status()).noContent();
		
	}
}