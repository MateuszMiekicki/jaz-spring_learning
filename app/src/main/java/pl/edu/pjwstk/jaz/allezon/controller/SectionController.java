package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pl.edu.pjwstk.jaz.allezon.DTO.SectionDTO;
import pl.edu.pjwstk.jaz.allezon.entity.SectionEntity;
import pl.edu.pjwstk.jaz.allezon.repository.SectionRepository;

import java.util.List;

@RestController
public class SectionController {
    private final SectionRepository sectionRepository;

    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("allezon/sections")
    public ResponseEntity<List<SectionEntity>> getSections() {
        return new ResponseEntity(sectionRepository.getSections(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/sections/add")
    public ResponseEntity<String> addSection(@RequestBody SectionDTO sectionDTO) {
        if (sectionRepository.findByName(sectionDTO.getName()) != null) {
            return new ResponseEntity<>("Such an section exists in the database.", HttpStatus.CONFLICT);
        }
        sectionRepository.addSection(sectionDTO);
        return new ResponseEntity("Added section", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/sections/delete")
    public ResponseEntity<String> deleteSection(@RequestBody SectionDTO sectionDTO) {
        SectionEntity sectionEntity = sectionRepository.findByName(sectionDTO.getName());
        if (sectionEntity == null) {
            return new ResponseEntity<>("Such an section not exists in the database.", HttpStatus.CONFLICT);
        }
        sectionRepository.deleteSection(sectionEntity);
        return new ResponseEntity("Deleted section", HttpStatus.NO_CONTENT);
    }
}
