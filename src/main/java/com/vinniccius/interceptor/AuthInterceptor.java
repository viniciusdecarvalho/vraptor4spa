package com.vinniccius.interceptor;

import static br.com.caelum.vraptor.view.Results.status;

import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Message;

import com.vinniccius.service.AuthService;

/**
 * Interceptor to check if the user is in the session.
 */
@Intercepts
@RequestScoped
public class AuthInterceptor {

	private AuthService authentication;
	private AuthCache authCache; 
	private Result result;
	
	/**
	 * @deprecated cdi eyes only
	 */
	public AuthInterceptor() {}
	
	@Inject
	public AuthInterceptor(Result result, AuthCache authCache, AuthService authentication) {
		this.result = result;
		this.authCache = authCache;
		this.authentication = authentication;
	}
	
	@Accepts
	public boolean accepts(ControllerMethod method) {
		return !method.containsAnnotation(Public.class);
	}

	@BeforeCall
	public void before(AuthUser userAuth) {		
		if (!authCache.contains(userAuth)) {
			if (authentication.authenticate(userAuth)) {
				authCache.put(userAuth);
			}
		}
	}
	
	/**
	 * Intercepts the request and checks if there is a user logged in.
	 */
	@AroundCall
	public void intercept(SimpleInterceptorStack stack, AuthUser userAuth) {
		if (authCache.contains(userAuth)) {
			stack.next();
		} else {
			result.use(status()).badRequest(Arrays.asList(notLoggedMessage()));			
		}
	}
	
	private Message notLoggedMessage() {
		return new I18nMessage("user", "user.not.logged");
	}

}
