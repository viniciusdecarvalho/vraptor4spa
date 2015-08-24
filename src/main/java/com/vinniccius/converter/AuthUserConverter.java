package com.vinniccius.converter;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

import com.vinniccius.interceptor.AuthUser;

@Convert(AuthUser.class)
public class AuthUserConverter implements Converter<AuthUser> {

	private static final Logger LOG = LoggerFactory.getLogger(AuthUserConverter.class);
	
	private HttpServletRequest request;
	
	/**
	 * @deprecated
	 */
	protected AuthUserConverter() {
		this(null);
	}
	
	public AuthUserConverter(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public AuthUser convert(String value, Class<? extends AuthUser> type) {
		
		String auth = request.getHeader(AuthUser.AUTHENTICATION_HEADER);
		final String encoded = auth.replaceFirst("Basic ", "");
		
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encoded);
			String[] values = new String(decodedBytes, "UTF-8").split(":");
			return new AuthUser(values[0], values[1]);
		} catch (Exception e) {
			LOG.debug("authentication decode failed", e);
		}
		
		return null;
	}

}
