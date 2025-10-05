package com.example.demo.api;

import com.example.demo.dto.LoanDTO;
import com.example.demo.model.Loan;
import com.example.demo.model.User;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.CustomUserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanRestController {

    private final LoanRepository loanRepo;

    public LoanRestController(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }

    // ✅ Admin & SuperAdmin เห็น loan ทั้งหมด
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> list = loanRepo.findAll().stream()
                .map(l -> new LoanDTO(l.getId(), l.getUser().getId(),
                        l.getEquipment().getId(), l.getBorrowedAt(), l.getReturnedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    // ✅ Member ดู loan ของตัวเอง
    @GetMapping("/me")
    public ResponseEntity<List<LoanDTO>> getMyLoans(@AuthenticationPrincipal CustomUserDetailsImpl principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User me = principal.getUser();
        List<LoanDTO> list = loanRepo.findByUser(me).stream()
                .map(l -> new LoanDTO(l.getId(), me.getId(),
                        l.getEquipment().getId(), l.getBorrowedAt(), l.getReturnedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    // ✅ Member ยืมอุปกรณ์
    @PostMapping("/borrow")
    public ResponseEntity<LoanDTO> borrow(@AuthenticationPrincipal CustomUserDetailsImpl principal,
                                          @RequestBody Loan loan) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User me = principal.getUser();
        loan.setUser(me);
        loan.setBorrowedAt(LocalDateTime.now());
        loan.setReturnedAt(null);
        Loan saved = loanRepo.save(loan);
        return ResponseEntity.ok(new LoanDTO(saved.getId(), me.getId(),
                saved.getEquipment().getId(), saved.getBorrowedAt(), saved.getReturnedAt()));
    }

    // ✅ Member คืนอุปกรณ์
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetailsImpl principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return loanRepo.findById(id)
                .map(loan -> {
                    loan.setReturnedAt(LocalDateTime.now());
                    Loan saved = loanRepo.save(loan);
                    return ResponseEntity.ok(new LoanDTO(saved.getId(), saved.getUser().getId(),
                            saved.getEquipment().getId(), saved.getBorrowedAt(), saved.getReturnedAt()));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
