package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PracticeScore;
import com.example.demo.model.User;

public interface PracticeScoreRepository extends JpaRepository<PracticeScore, Long> {

	// หาทุก score ของ user ที่ระบุ
	List<PracticeScore> findByUser(User user);
}
