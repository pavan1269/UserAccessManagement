package com.useraccess.management.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class BearerToken extends AbstractAuthenticationToken{

	final private String token;
	
	public BearerToken(Collection<? extends GrantedAuthority> authorities, String token) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.token = token;
	}

	@Override
	public String getCredentials() {
		// TODO Auto-generated method stub
		return token;
	}

	@Override
	public String getPrincipal() {
		// TODO Auto-generated method stub
		return token;
	}

}
