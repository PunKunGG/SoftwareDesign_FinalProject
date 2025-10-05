package com.example.demo.api;

import com.example.demo.dto.PracticeScoreDTO;
import com.example.demo.model.PracticeScore;
import com.example.demo.model.User;
import com.example.demo.repository.PracticeScoreRepository;
import com.example.demo.service.CustomUserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/practice")
public class PracticeScoreRestController {

	private final PracticeScoreRepository practiceRepo;

	public PracticeScoreRestController(PracticeScoreRepository practiceRepo) {
		this.practiceRepo = practiceRepo;
	}

	// ✅ Member ดูคะแนนของตัวเอง
	@GetMapping("/me")
	public ResponseEntity<List<PracticeScoreDTO>> getMyScores(
			@AuthenticationPrincipal CustomUserDetailsImpl principal) {
		if (principal == null) {
			return ResponseEntity.status(401).build();
		}
		User me = principal.getUser();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		List<PracticeScoreDTO> list = practiceRepo.findByUser(me).stream()
				.map(s -> new PracticeScoreDTO(s.getId(), me.getId(), s.getDistance(), s.getBowType(), s.getArrows(),
						s.getScore(), s.getCreatedAt() != null ? s.getCreatedAt().format(formatter) : "" // 🔥 format
																											// เป็น
																											// String
				)).toList();
		return ResponseEntity.ok(list);
	}

	// ✅ Member เพิ่มคะแนนซ้อม
	@PostMapping
	public ResponseEntity<PracticeScoreDTO> create(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@RequestBody PracticeScore score) {
		if (principal == null) {
			return ResponseEntity.status(401).build();
		}
		User me = principal.getUser();
		score.setUser(me);
		PracticeScore saved = practiceRepo.save(score);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		return ResponseEntity.ok(new PracticeScoreDTO(saved.getId(), me.getId(), saved.getDistance(),
				saved.getBowType(), saved.getArrows(), saved.getScore(),
				saved.getCreatedAt() != null ? saved.getCreatedAt().format(formatter) : ""));
	}
}
