package com.vinniccius.interceptor;

import javax.enterprise.context.ApplicationScoped;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@ApplicationScoped
public class AuthCache {

	private Cache<Integer, AuthUser> cache;

	public AuthCache() {
		this.cache = CacheBuilder.newBuilder().build();
	}
	
	public void put(AuthUser user) {
		if (user != null) {
			this.cache.put(token(user), user);
		}
	}
	
	public boolean contains(AuthUser user) {
		if (user == null) {
			return false;
		}
		return this.cache.getIfPresent(token(user)) != null;
	}
	
	public void remove(AuthUser user) {
		if (user != null) {
			this.cache.invalidate(token(user));
		}
	}

	private int token(AuthUser user) {
		return user.hashCode();
	}
	
}
