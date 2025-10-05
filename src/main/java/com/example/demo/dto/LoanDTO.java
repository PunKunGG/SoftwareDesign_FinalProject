package com.example.demo.dto;

import java.time.LocalDateTime;

public class LoanDTO {
	private Long id;
	private Long userId; // เอาแค่ id ของ user
	private Long equipmentId; // เอาแค่ id ของอุปกรณ์
	private LocalDateTime borrowedAt;
	private LocalDateTime returnedAt;

	public LoanDTO(Long id, Long userId, Long equipmentId, LocalDateTime borrowedAt, LocalDateTime returnedAt) {
		this.id = id;
		this.userId = userId;
		this.equipmentId = equipmentId;
		this.borrowedAt = borrowedAt;
		this.returnedAt = returnedAt;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public LocalDateTime getBorrowedAt() {
		return borrowedAt;
	}

	public LocalDateTime getReturnedAt() {
		return returnedAt;
	}
}
