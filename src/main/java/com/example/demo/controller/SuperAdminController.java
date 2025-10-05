package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.Announcement;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.CustomUserDetailsImpl;

@Controller
@RequestMapping("/super")
public class SuperAdminController {

	private final UserRepository userRepo;
	private final AnnouncementRepository announcementRepo;
	private final LoanRepository loanRepo;

	public SuperAdminController(UserRepository userRepo, AnnouncementRepository announcementRepo,
			LoanRepository loanRepo) {
		this.userRepo = userRepo;
		this.announcementRepo = announcementRepo;
		this.loanRepo = loanRepo;
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<UserDTO> users = userRepo.findAll().stream().map(
				u -> new UserDTO(u.getId(), u.getEmail(), u.getUsername(), u.getPhone(), u.getStudentid(), u.getRole()))
				.toList();

		model.addAttribute("users", users);
		model.addAttribute("roles", Role.values()); // ✅ ส่ง enum ทั้งหมดไปให้ dropdown
		return "userManagement";
	}

	@PostMapping("/users/{id}/role")
	public String updateRole(@PathVariable Long id, @RequestParam Role role) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
		user.setRole(role);
		userRepo.save(user);
		return "redirect:/super/users";
	}

	@GetMapping({ "", "/", "/home" })
	public String superDashboard(Model model, @AuthenticationPrincipal CustomUserDetailsImpl principal) {
		User me = principal.getUser();
		model.addAttribute("user", me);
		model.addAttribute("role", me.getRole());

		// ✅ เพิ่มประกาศ และการยืมที่ยังไม่คืน
		model.addAttribute("announcements", announcementRepo.findAll(Sort.by(Sort.Direction.DESC, "publishedAt")));
		model.addAttribute("activeLoans", loanRepo.findByReturnedAtIsNull());

		return "superDashboard";
	}
}
