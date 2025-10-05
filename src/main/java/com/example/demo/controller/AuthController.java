package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.UserService;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam String studentid,
			@RequestParam String phone, @RequestParam String email, @RequestParam String password) {
		userService.registerUser(username, studentid, phone, email, password);
		return "redirect:/adminDashboard"; // ไปหน้า welcome ได้เลย
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/adminDashboard")
	public String welcome() {
		return "adminDashboard";
	}

	// ✅ Forgot Password GET
	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot-password";
	}

	// ✅ Forgot Password POST
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email, Model model) {
		// TODO: เรียก userService เพื่อทำระบบจริง เช่นส่งอีเมล reset
		if (userService.existsByEmail(email)) {
			model.addAttribute("success", "A reset link has been sent to your email.");
		} else {
			model.addAttribute("error", "Email not found in the system.");
		}
		return "forgot-password";
	}
}
