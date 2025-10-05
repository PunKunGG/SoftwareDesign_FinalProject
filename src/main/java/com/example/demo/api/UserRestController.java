package com.example.demo.api;

import com.example.demo.model.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepo;

    public UserRestController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ Super Admin เห็นผู้ใช้ทั้งหมด
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepo.findAll().stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    // ✅ ดูผู้ใช้ตาม id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Super Admin สร้าง user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        if (user.getRole() == null) {
            user.setRole(Role.MEMBER); // default MEMBER
        }
        User saved = userRepo.save(user);
        return ResponseEntity.ok(toDTO(saved));
    }

    // ✅ Super Admin อัปเดต user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userRepo.findById(id).map(u -> {
            u.setUsername(newUser.getUsername());
            u.setEmail(newUser.getEmail());
            u.setPhone(newUser.getPhone());
            u.setStudentid(newUser.getStudentid());
            u.setRole(newUser.getRole());
            User updated = userRepo.save(u);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Super Admin ลบ user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Member, Admin, SuperAdmin ดูข้อมูลของตัวเอง
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetailsImpl principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(toDTO(principal.getUser()));
    }

    // 🔹 Helper method แปลง User → UserDTO
    private UserDTO toDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getEmail(),
                u.getUsername(),
                u.getPhone(),
                u.getStudentid(),
                u.getRole()
        );
    }
}
