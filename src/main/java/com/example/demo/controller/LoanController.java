package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.CustomUserDetailsImpl;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanController {

	private final LoanRepository loanRepo;
	private final EquipmentRepository equipmentRepo; // ✅ inject repo

	public LoanController(LoanRepository loanRepo, EquipmentRepository equipmentRepo) {
		this.loanRepo = loanRepo;
		this.equipmentRepo = equipmentRepo; // ✅ เก็บไว้ใน field
	}

	// ✅ Member เห็นเฉพาะ loan ของตัวเอง
	@GetMapping("/my")
	public String myLoans(@AuthenticationPrincipal CustomUserDetailsImpl principal, Model model) {
		if (principal == null) {
			return "redirect:/login";
		}
		User me = principal.getUser();
		List<Loan> myLoans = loanRepo.findByUser(me);
		model.addAttribute("loans", myLoans);
		return "loanManagement";
	}

	// ✅ Admin/SuperAdmin เห็นทั้งหมด
	@GetMapping("/all")
	public String allLoans(Model model) {
		model.addAttribute("loans", loanRepo.findAll());
		return "loanManagement";
	}

	// ✅ default → redirect ตาม role
	@GetMapping
	public String defaultLoans(@AuthenticationPrincipal CustomUserDetailsImpl principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		User me = principal.getUser();

		if (me.getRole().equals("ROLE_MEMBER")) {
			return "redirect:/loans/my";
		} else {
			return "redirect:/loans/all";
		}
	}

	// ✅ ยืมอุปกรณ์
	@PostMapping("/borrow/{equipmentId}")
	public String borrowEquipment(@PathVariable Long equipmentId, @RequestParam(defaultValue = "1") int quantity,
			@AuthenticationPrincipal CustomUserDetailsImpl principal, RedirectAttributes redirectAttrs) {
		User user = principal.getUser();
		Equipment eq = equipmentRepo.findById(equipmentId).orElseThrow();

		// ✅ ตรวจสอบว่า stock พอไหม
		if (eq.getQuantity() < quantity) {
			redirectAttrs.addFlashAttribute("error",
					"❌ จำนวนอุปกรณ์ไม่เพียงพอ (" + eq.getQuantity() + " ชิ้นที่เหลืออยู่)");
			return "redirect:/equipment"; // 🔹 กลับไปหน้ารายการอุปกรณ์
		}

		// ✅ ตรวจสอบสถานะด้วย (กันลืม)
		if (!"Available".equalsIgnoreCase(eq.getStatus())) {
			redirectAttrs.addFlashAttribute("error", "❌ อุปกรณ์นี้ไม่พร้อมให้ยืม (" + eq.getStatus() + ")");
			return "redirect:/equipment";
		}

		Loan loan = new Loan();
		loan.setEquipment(eq);
		loan.setUser(user);
		loan.setQuantity(quantity);
		loan.setBorrowedAt(LocalDateTime.now());

		loanRepo.save(loan);

		// ✅ ลดจำนวน stock ด้วย
		eq.setQuantity(eq.getQuantity() - quantity);

		// ถ้าหลังยืมจนหมด → เปลี่ยนสถานะเป็น Borrowed
		if (eq.getQuantity() <= 0) {
			eq.setStatus("Borrowed");
		}

		equipmentRepo.save(eq);

		redirectAttrs.addFlashAttribute("success",
				"✅ ยืมอุปกรณ์ " + eq.getName() + " จำนวน " + quantity + " ชิ้น สำเร็จ!");
		return "redirect:/equipment";
	}

	// ✅ คืนอุปกรณ์
	@PostMapping("/return/{loanId}")
	public String returnLoan(@PathVariable Long loanId, @AuthenticationPrincipal CustomUserDetailsImpl principal) {
		Loan loan = loanRepo.findById(loanId).orElseThrow();

		User currentUser = principal.getUser();
		boolean isAdmin = principal.getAuthorities().stream().anyMatch(
				auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SUPER_ADMIN"));

		if (!isAdmin && !loan.getUser().getId().equals(currentUser.getId())) {
			return "redirect:/loans/my?error=unauthorized";
		}

		// อัปเดตสถานะการคืน
		loan.setReturnedAt(LocalDateTime.now());
		loanRepo.save(loan);

		// อัปเดตจำนวนอุปกรณ์
		Equipment eq = loan.getEquipment();
		eq.setQuantity(eq.getQuantity() + loan.getQuantity()); // ✅ คืนจำนวนกลับ
		eq.setStatus("Available");
		equipmentRepo.save(eq);

		return isAdmin ? "redirect:/loans/all" : "redirect:/loans/my";
	}

}
