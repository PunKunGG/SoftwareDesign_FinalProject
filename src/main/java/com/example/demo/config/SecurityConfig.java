package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.CustomUserDetails;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final CustomUserDetails customUserDetails;
	private final CustomSuccessHandler customSuccessHandler;

	public SecurityConfig(CustomUserDetails customUserDetails, CustomSuccessHandler customSuccessHandler) {
		this.customUserDetails = customUserDetails;
		this.customSuccessHandler = customSuccessHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				// public
				.requestMatchers("/login", "/welcome", "/register", "/register/**", "/forgot-password", "/css/**", "/js/**", "/img/**")
				.permitAll()

				// admin, super
				.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN")

				// super admin เท่านั้น
				.requestMatchers("/super/**").hasAuthority("ROLE_SUPER_ADMIN")

				// api
				.requestMatchers("/api/users/me").hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN")
				.requestMatchers("/api/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN")

				// dashboard, member
				.requestMatchers("/dashboard/**", "/member/**")
				.hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN")

				// อื่น ๆ ต้อง login
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("email")
						.passwordParameter("password").successHandler(customSuccessHandler) // ✅ ใช้ handler
						.failureUrl("/login?error").permitAll())
				.logout(logout -> logout.permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
