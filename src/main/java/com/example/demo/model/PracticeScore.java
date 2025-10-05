package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PracticeScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int distance;
	private String bowType;
	private int arrows;
	private int score;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// ✅ ฟิลด์วันที่สร้าง
	private LocalDateTime createdAt;

	// --- Constructors ---
	public PracticeScore() {
	}

	public PracticeScore(User user, int distance, String bowType, int arrows, int score) {
		this.user = user;
		this.distance = distance;
		this.bowType = bowType;
		this.arrows = arrows;
		this.score = score;
		this.createdAt = LocalDateTime.now(); // ✅ กำหนดเวลาอัตโนมัติ
	}

	// --- Getter & Setter ---
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getBowType() {
		return bowType;
	}

	public void setBowType(String bowType) {
		this.bowType = bowType;
	}

	public int getArrows() {
		return arrows;
	}

	public void setArrows(int arrows) {
		this.arrows = arrows;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
