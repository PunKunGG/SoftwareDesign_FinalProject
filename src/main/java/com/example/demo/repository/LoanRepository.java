package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Equipment;
import com.example.demo.model.Loan;
import com.example.demo.model.User;

public interface LoanRepository extends JpaRepository<Loan, Long>{
	List<Loan> findByUser(User user);
	
	List<Loan> findByReturnedAtIsNull();
	
	List<Loan> findByUserAndReturnedAtIsNull(User user);
	
	List<Loan> findByUserAndReturnedAtIsNullOrderByBorrowedAtDesc(User user);
	
	Optional<Loan> findFirstByUserAndEquipmentAndReturnedAtIsNull(User user, Equipment equipment);
	
	@Query("""
		       select l from Loan l
		       join fetch l.equipment e
		       where l.user = :user and l.returnedAt is null
		       order by l.borrowedAt desc
		       """)
		List<Loan> findActiveWithEquipByUser(@Param("user") User user);
}
