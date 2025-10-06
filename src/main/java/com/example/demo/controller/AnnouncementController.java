package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.model.Announcement;
import com.example.demo.model.User;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.service.CustomUserDetailsImpl;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	private final AnnouncementRepository announcementRepo;

	public AnnouncementController(AnnouncementRepository announcementRepo) {
		this.announcementRepo = announcementRepo;
	}

	// ✅ แสดงรายการประกาศทั้งหมด
	@GetMapping
	public String list(Model model) {
		model.addAttribute("announcements", announcementRepo.findAll());
		return "announcementManagement";
	}

	// ✅ เพิ่มประกาศใหม่
	@PostMapping
	public String createAnnouncement(@ModelAttribute Announcement announcement,
			@AuthenticationPrincipal CustomUserDetailsImpl principal, RedirectAttributes redirectAttrs) {

		User author = principal.getUser();
		announcement.setAuthor(author); // ✅ กำหนดผู้เขียน
		announcement.setPublishedAt(LocalDateTime.now());
		announcementRepo.save(announcement);

		redirectAttrs.addFlashAttribute("success", "เพิ่มประกาศใหม่เรียบร้อยแล้ว!");
		return "redirect:/announcements";
	}

	// ✅ อัปเดตประกาศ
	@PostMapping("/{id}")
	public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement updatedAnnouncement,
			@AuthenticationPrincipal CustomUserDetailsImpl principal, RedirectAttributes redirectAttrs) {

		Announcement existing = announcementRepo.findById(id).orElseThrow();
		User author = principal.getUser();

		existing.setTitle(updatedAnnouncement.getTitle());
		existing.setSummary(updatedAnnouncement.getSummary());
		existing.setContent(updatedAnnouncement.getContent());
		existing.setImageUrl(updatedAnnouncement.getImageUrl());
		existing.setAuthor(author); // ✅ อัปเดตชื่อผู้เขียนด้วย
		existing.setPublishedAt(LocalDateTime.now());

		announcementRepo.save(existing);

		redirectAttrs.addFlashAttribute("success", "แก้ไขประกาศเรียบร้อยแล้ว!");
		return "redirect:/announcements";
	}
	
	@GetMapping("/new")
	public String newForm(Model model) {
	    model.addAttribute("announcement", new Announcement());
	    return "announcementForm"; // ใช้หน้าเดียวกับตอนแก้ไข
	}

	// ✅ หน้าแก้ไข
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		Announcement ann = announcementRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ไม่พบประกาศ ID: " + id));
		model.addAttribute("announcement", ann);
		return "announcementForm"; // ใช้หน้าเดียวกับตอนเพิ่ม
	}

	// ✅ ลบประกาศ
	@GetMapping("/delete/{id}")
	public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttrs) {
		announcementRepo.deleteById(id);
		redirectAttrs.addFlashAttribute("success", "ลบประกาศเรียบร้อยแล้ว!");
		return "redirect:/announcements";
	}

	// ✅ ดูรายละเอียด
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		Announcement ann = announcementRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ไม่พบประกาศ ID: " + id));
		model.addAttribute("announcement", ann);
		return "announcement_detail";
	}
}
