package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Equipment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name; // เช่น ธนู Recurve #1
	private int quantity; // จำนวนที่เหลือให้ยืม
	private String status; // Available, Borrowed, Maintenance
	private String category; // ✅ เพิ่มประเภท เช่น "Bow", "Arrow", "Target"

	// ---- Getter/Setter ----
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
