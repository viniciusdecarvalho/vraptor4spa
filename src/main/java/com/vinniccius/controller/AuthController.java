package com.vinniccius.controller;

import static br.com.caelum.vraptor.view.Results.status;

import java.util.Arrays;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.Validator;

import com.vinniccius.interceptor.AuthCache;
import com.vinniccius.interceptor.AuthUser;
import com.vinniccius.interceptor.Public;
import com.vinniccius.service.AuthService;

@Controller
public class AuthController {

	private Result result;
	private Validator validator;
	private AuthService authorization;
	private AuthCache authCache;
	
	/**
	 * @deprecated cdi eyes only
	 */
	protected AuthController() {
		this(null, null, null, null);
	}
	
	@Inject
	public AuthController(Result result, Validator validator, AuthService authorization, AuthCache cache) {
		this.result = result;
		this.validator = validator;
		this.authorization = authorization;
		this.authCache = cache;
	}
	
	@Public
	@Post("/login")
	public void login(AuthUser user) {
		validator.validate(user)
				 .onErrorUse(status()).badRequest(validator.getErrors());
		
		boolean contains = authorization.authenticate(user);
		if (contains) {
			authCache.put(user);
			result.use(status()).ok();
		} else {
			Message message = new I18nMessage("user", "user.with.login.invalid");
			result.use(status()).badRequest(Arrays.asList(message));
		}
	}

	@Public
	@Post("/logout")
	public void logout(AuthUser user) {
		authCache.remove(user);
		result.use(status()).ok();
	}
}
