package com.example.demo.service;

//import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void registerUser(String username,String studentid,String phone,String email,String password) {
		User user = new User();
		user.setUsername(username);
		user.setStudentid(studentid);
		user.setPhone(phone);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.MEMBER);
		userRepository.save(user);
		
		 UsernamePasswordAuthenticationToken authToken =
			        new UsernamePasswordAuthenticationToken(
			            user.getUsername(),
			            password,
			            org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_"+user.getRole().name())
			        );
			    SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	public boolean existsByEmail(String email) {
	    return userRepository.findByEmail(email).isPresent();
	}


}
