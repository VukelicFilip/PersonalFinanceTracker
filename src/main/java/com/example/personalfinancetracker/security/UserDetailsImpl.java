package com.example.personalfinancetracker.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multi.tenant.entity.UserEntity;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private String tenantIdentifier;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String email, String password, String username,
			Collection<? extends GrantedAuthority> authorities, String tenantIdentifier) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.username = username;
		this.tenantIdentifier = tenantIdentifier;
	}

	public static UserDetailsImpl build(UserEntity user) {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));

		return new UserDetailsImpl(user.getUserEntityId(), user.getEmail(), user.getPassword(), user.getEmail(),
				authorities, user.getTenantIdentifier());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getTenantIdentifier() {
		return tenantIdentifier;
	}

	public void setTenantIdentifier(String tenantIdentifier) {
		this.tenantIdentifier = tenantIdentifier;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
