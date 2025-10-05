package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Equipment equipment;

	@ManyToOne
	private User user;

	private int quantity;
	private LocalDateTime borrowedAt;
	private LocalDateTime returnedAt;

	// ----- Getter & Setter -----
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getBorrowedAt() {
		return borrowedAt;
	}

	public void setBorrowedAt(LocalDateTime borrowedAt) {
		this.borrowedAt = borrowedAt;
	}

	public LocalDateTime getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(LocalDateTime returnedAt) {
		this.returnedAt = returnedAt;
	}
}
