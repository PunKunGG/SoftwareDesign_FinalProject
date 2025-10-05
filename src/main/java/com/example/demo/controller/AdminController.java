package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.CustomUserDetailsImpl;

import com.example.demo.model.User;
import com.example.demo.model.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.LoanRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final AnnouncementRepository announcementRepo;
	private final LoanRepository loanRepo;

	public AdminController(AnnouncementRepository announcementRepo, LoanRepository loanRepo) {
		this.announcementRepo = announcementRepo;
		this.loanRepo = loanRepo;
	}

	// ✅ Dashboard
	@GetMapping({ "", "/", "welcome" })
	public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetailsImpl principal) {
		model.addAttribute("announcements", announcementRepo.findAll(Sort.by(Sort.Direction.DESC, "publishedAt")));
		model.addAttribute("activeLoans", loanRepo.findByReturnedAtIsNull());

		User me = principal.getUser();
		model.addAttribute("user", me);
		model.addAttribute("role", me.getRole());

		return "adminDashboard";
	}

	// ✅ Manage Announcements
	@GetMapping("/announcements")
	public String listAnnouncements(Model model) {
		model.addAttribute("announcements", announcementRepo.findAll(Sort.by(Sort.Direction.DESC, "publishedAt")));
		return "announcementManagement";
	}

	// ➕ สร้างใหม่
	@GetMapping("/announcements/new")
	public String newAnnouncementForm(Model model) {
		model.addAttribute("announcement", new Announcement());
		return "announcementForm";
	}

	@PostMapping("/announcements/save")
	public String saveAnnouncement(@ModelAttribute Announcement announcement, @RequestParam("file") MultipartFile file,
			@AuthenticationPrincipal CustomUserDetailsImpl principal) throws IOException {

		if (!file.isEmpty()) {
			String uploadDir = "src/main/resources/static/uploads/";
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path path = Paths.get(uploadDir + fileName);

			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());

			announcement.setImageUrl("/uploads/" + fileName);
		}

		if (principal != null) {
			announcement.setAuthor(principal.getUser()); // ✅ ปลอดภัย
		} else {
			// กัน error เวลา principal null
			System.out.println("⚠️ principal is null (user not logged in?)");
		}

		announcementRepo.save(announcement);

		return "redirect:/admin/announcements";
	}

	// ✏️ แก้ไข
	@GetMapping("/announcements/edit/{id}")
	public String editAnnouncement(@PathVariable Long id, Model model) {
		Announcement ann = announcementRepo.findById(id).orElseThrow();
		model.addAttribute("announcement", ann);
		return "announcementForm";
	}

	@PostMapping("/announcements/update/{id}")
	public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement announcement) {
		Announcement ann = announcementRepo.findById(id).orElseThrow();
		ann.setTitle(announcement.getTitle());
		ann.setSummary(announcement.getSummary());
		ann.setImageUrl(announcement.getImageUrl());
		ann.setContent(announcement.getContent());
		announcementRepo.save(ann);
		return "redirect:/admin/announcements";
	}

	// ❌ ลบ
	@GetMapping("/announcements/delete/{id}")
	public String deleteAnnouncement(@PathVariable Long id) {
		announcementRepo.deleteById(id);
		return "redirect:/admin/announcements";
	}
}
