package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Loan;
import com.example.demo.model.Announcement;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.service.CustomUserDetailsImpl;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MemberController {

	private final LoanRepository loanRepo;
	private final AnnouncementRepository announcementRepo;

	public MemberController(LoanRepository loanRepo, AnnouncementRepository announcementRepo) {
		this.loanRepo = loanRepo;
		this.announcementRepo = announcementRepo;
	}

	@GetMapping("/dashboard")
	public String memberDashboard(Model model, @AuthenticationPrincipal CustomUserDetailsImpl principal) {
		User me = principal.getUser();

		// ✅ อุปกรณ์ที่ยังไม่คืน
		List<Loan> myLoans = loanRepo.findByUserAndReturnedAtIsNull(me);

		// ✅ ประกาศล่าสุด 5 อัน
		List<Announcement> latestAnnouncements = announcementRepo.findTop5ByOrderByPublishedAtDesc();

		model.addAttribute("user", me);
		model.addAttribute("role", me.getRole());
		model.addAttribute("myLoans", myLoans);
		model.addAttribute("announcements", latestAnnouncements);

		return "dashboard"; // ใช้ dashboard.html
	}
}
