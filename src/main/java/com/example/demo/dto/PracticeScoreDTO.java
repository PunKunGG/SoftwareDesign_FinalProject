package com.example.demo.dto;

public class PracticeScoreDTO {
	private Long id;
	private Long userId;
	private int distance;
	private String bowType;
	private int arrows;
	private int score;
	private String createdAt; // 🔥 เปลี่ยนเป็น String

	public PracticeScoreDTO(Long id, Long userId, int distance, String bowType, int arrows, int score,
			String createdAt) {
		this.id = id;
		this.userId = userId;
		this.distance = distance;
		this.bowType = bowType;
		this.arrows = arrows;
		this.score = score;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public int getDistance() {
		return distance;
	}

	public String getBowType() {
		return bowType;
	}

	public int getArrows() {
		return arrows;
	}

	public int getScore() {
		return score;
	}

	public String getCreatedAt() {
		return createdAt;
	}
}
