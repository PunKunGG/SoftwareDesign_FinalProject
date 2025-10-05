package com.example.demo.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.PracticeScoreDTO;
import com.example.demo.model.PracticeScore;
import com.example.demo.model.User;
import com.example.demo.repository.PracticeScoreRepository;
import com.example.demo.service.CustomUserDetailsImpl;

@Controller
@RequestMapping("/practice")
public class PracticeController {

	private final PracticeScoreRepository practiceRepo;

	public PracticeController(PracticeScoreRepository practiceRepo) {
		this.practiceRepo = practiceRepo;
	}

	@GetMapping
	public String practiceForm(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
		User user = userDetails.getUser();
		model.addAttribute("scores", practiceRepo.findByUser(user));
		return "practiceForm";
	}

	@PostMapping
	public String savePractice(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, @RequestParam int distance,
			@RequestParam String bowType, @RequestParam int arrows, @RequestParam int score,
			RedirectAttributes redirectAttrs) {
		User user = userDetails.getUser();
		PracticeScore ps = new PracticeScore(user, distance, bowType, arrows, score);
		practiceRepo.save(ps);

		redirectAttrs.addFlashAttribute("success", "บันทึกคะแนนสำเร็จ!");
		return "redirect:/practice";
	}

	@GetMapping("/list")
	public String practiceList(@AuthenticationPrincipal CustomUserDetailsImpl userDetails, Model model) {
		User user = userDetails.getUser();
		var scores = practiceRepo.findByUser(user);

		// ✅ map เป็น DTO เพื่อเลี่ยง LocalDateTime
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		var dtoList = scores.stream()
				.map(s -> new PracticeScoreDTO(s.getId(), user.getId(), s.getDistance(), s.getBowType(), s.getArrows(),
						s.getScore(), s.getCreatedAt() != null ? s.getCreatedAt().format(formatter) : ""))
				.toList();

		// ✅ คำนวณสถิติ
		double avgScore = scores.stream().mapToInt(PracticeScore::getScore).average().orElse(0);
		double avgDistance = scores.stream().mapToInt(PracticeScore::getDistance).average().orElse(0);
		int totalArrows = scores.stream().mapToInt(PracticeScore::getArrows).sum();
		int totalSessions = scores.size();

		model.addAttribute("scores", dtoList);
		model.addAttribute("avgScore", avgScore);
		model.addAttribute("avgDistance", avgDistance);
		model.addAttribute("totalArrows", totalArrows);
		model.addAttribute("totalSessions", totalSessions);

		return "practiceList";
	}

}
