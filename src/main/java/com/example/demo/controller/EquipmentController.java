package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

	private final EquipmentRepository equipmentRepo;

	public EquipmentController(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}

	// ✅ แสดงรายการอุปกรณ์ทั้งหมด
	@GetMapping
	public String listEquipment(Model model) {
		List<Equipment> equipments = equipmentRepo.findAll();
		model.addAttribute("equipments", equipments);
		return "equipment/list";
	}

	// ✅ หน้าเพิ่มอุปกรณ์
	@GetMapping("/new")
	public String showForm(Model model) {
		model.addAttribute("equipment", new Equipment());
		return "equipment/form";
	}

	// ✅ บันทึกอุปกรณ์ใหม่
	@PostMapping
	public String save(@ModelAttribute Equipment equipment) {
		equipmentRepo.save(equipment);
		return "redirect:/equipment";
	}

	// ✅ หน้าแก้ไข
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Equipment eq = equipmentRepo.findById(id).orElseThrow();
		model.addAttribute("equipment", eq);
		return "equipment/form";
	}

	// ✅ อัปเดตอุปกรณ์
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Equipment newEq) {
		return equipmentRepo.findById(id).map(eq -> {
			eq.setName(newEq.getName());
			eq.setCategory(newEq.getCategory());
			eq.setQuantity(newEq.getQuantity());
			eq.setStatus(newEq.getStatus());
			equipmentRepo.save(eq);
			return "redirect:/equipment";
		}).orElse("redirect:/equipment");
	}

	// ✅ ลบอุปกรณ์
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		equipmentRepo.deleteById(id);
		return "redirect:/equipment";
	}
}
