package com.example.demo.controller;

import com.example.demo.model.PracticeScore;
import com.example.demo.model.User;
import com.example.demo.repository.PracticeScoreRepository;
import com.example.demo.service.CustomUserDetailsImpl;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/practice")
public class PracticeScoreController {

	private final PracticeScoreRepository practiceRepo;

	public PracticeScoreController(PracticeScoreRepository practiceRepo) {
		this.practiceRepo = practiceRepo;
	}

	// ✅ Member ดูคะแนนของตัวเอง
	@GetMapping("/my")
	public String myScores(@AuthenticationPrincipal CustomUserDetailsImpl principal, Model model) {
		if (principal == null) {
			return "redirect:/login";
		}
		User me = principal.getUser();
		List<PracticeScore> scores = practiceRepo.findByUser(me);
		model.addAttribute("scores", scores);
		return "practiceManagement";
	}

	// ✅ Admin / SuperAdmin ดูคะแนนทั้งหมด
	@GetMapping("/all")
	public String allScores(Model model) {
		model.addAttribute("scores", practiceRepo.findAll());
		return "practiceManagement";
	}

}
