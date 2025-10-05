package com.example.demo.dto;

import com.example.demo.model.Role;

public class UserDTO {
	private Long id;
	private String email;
	private String username;
	private String phone;
	private String studentid;
	private Role role;

	public UserDTO(Long id, String email, String username, String phone, String studentid, Role role) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.phone = phone;
		this.studentid = studentid;
		this.role = role;
	}

	// Getter เท่านั้น (ไม่ต้องมี password)
	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPhone() {
		return phone;
	}

	public String getStudentid() {
		return studentid;
	}

	public Role getRole() {
		return role;
	}
}
