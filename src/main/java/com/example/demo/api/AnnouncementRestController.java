package com.example.demo.api;

import com.example.demo.dto.AnnouncementDTO;
import com.example.demo.model.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementRestController {

    private final AnnouncementRepository announcementRepo;

    public AnnouncementRestController(AnnouncementRepository announcementRepo) {
        this.announcementRepo = announcementRepo;
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAll() {
        List<AnnouncementDTO> list = announcementRepo.findAll().stream()
                .map(a -> new AnnouncementDTO(a.getId(), a.getTitle(), a.getSummary(),
                        a.getImageUrl(), a.getPublishedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> create(@RequestBody Announcement announcement) {
        Announcement saved = announcementRepo.save(announcement);
        return ResponseEntity.ok(new AnnouncementDTO(saved.getId(), saved.getTitle(),
                saved.getSummary(), saved.getImageUrl(), saved.getPublishedAt()));
    }
}
