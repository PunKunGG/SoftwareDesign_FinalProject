package com.example.demo.repository;

import com.example.demo.model.Announcement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	
	List<Announcement> findTop5ByOrderByPublishedAtDesc();


}
