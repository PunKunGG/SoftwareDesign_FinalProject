package com.example.demo.dto;

import java.time.LocalDateTime;

public class AnnouncementDTO {
	private Long id;
	private String title;
	private String summary;
	private String imageUrl;
	private LocalDateTime publishedAt;

	public AnnouncementDTO(Long id, String title, String summary, String imageUrl, LocalDateTime publishedAt) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.imageUrl = imageUrl;
		this.publishedAt = publishedAt;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public LocalDateTime getPublishedAt() {
		return publishedAt;
	}
}
