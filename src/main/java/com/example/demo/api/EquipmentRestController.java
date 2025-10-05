package com.example.demo.api;

import com.example.demo.dto.EquipmentDTO;
import com.example.demo.model.Equipment;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentRestController {

    private final EquipmentRepository equipmentRepo;

    public EquipmentRestController(EquipmentRepository equipmentRepo) {
        this.equipmentRepo = equipmentRepo;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAll() {
        List<EquipmentDTO> list = equipmentRepo.findAll().stream()
                .map(e -> new EquipmentDTO(e.getId(), e.getName(), e.getQuantity(), e.getStatus()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> create(@RequestBody Equipment equipment) {
        Equipment saved = equipmentRepo.save(equipment);
        return ResponseEntity.ok(new EquipmentDTO(saved.getId(), saved.getName(),
                saved.getQuantity(), saved.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> update(@PathVariable Long id, @RequestBody Equipment equipment) {
        return equipmentRepo.findById(id).map(e -> {
            e.setName(equipment.getName());
            e.setQuantity(equipment.getQuantity());
            e.setStatus(equipment.getStatus());
            Equipment saved = equipmentRepo.save(e);
            return ResponseEntity.ok(new EquipmentDTO(saved.getId(), saved.getName(),
                    saved.getQuantity(), saved.getStatus()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!equipmentRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipmentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
