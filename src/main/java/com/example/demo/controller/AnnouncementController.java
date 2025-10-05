package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Announcement;
import com.example.demo.repository.AnnouncementRepository;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	private final AnnouncementRepository announcementRepo;

	public AnnouncementController(AnnouncementRepository announcementRepo) {
		this.announcementRepo = announcementRepo;
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		Announcement ann = announcementRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ไม่พบประกาศ ID: " + id));
		model.addAttribute("announcement", ann);
		return "announcement_detail";
	}

}
