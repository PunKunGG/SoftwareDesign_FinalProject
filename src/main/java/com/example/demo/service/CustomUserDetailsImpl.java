package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class CustomUserDetailsImpl implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L; // << กำหนดค่าคงที่

	private final User user;

	public CustomUserDetailsImpl(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getStudentid() {
		return user.getStudentid();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getPhone() {
		return user.getPhone();
	}

	public Role getRole() {
		return user.getRole();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> "ROLE_" + user.getRole().name());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
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
}
