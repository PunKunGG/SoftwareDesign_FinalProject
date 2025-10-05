package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Equipment;
import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

	// 🔍 หาอุปกรณ์ทั้งหมดตามสถานะ (เช่น Available, Borrowed, Maintenance)
	List<Equipment> findByStatus(String status);

	// 🔍 หาอุปกรณ์ตามชื่อ (กรณีค้นหา)
	Optional<Equipment> findByName(String name);

	// 🔍 หาอุปกรณ์ที่ยัง Available
	List<Equipment> findByStatusOrderByNameAsc(String status);

	// 🔍 หาอุปกรณ์ตามหมวดหมู่
	List<Equipment> findByCategory(String category);
}
