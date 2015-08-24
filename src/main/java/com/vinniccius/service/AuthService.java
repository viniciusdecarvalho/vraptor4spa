package com.vinniccius.service;

import com.vinniccius.interceptor.AuthUser;

public interface AuthService {

	boolean authenticate(AuthUser userAuth);
	
}
