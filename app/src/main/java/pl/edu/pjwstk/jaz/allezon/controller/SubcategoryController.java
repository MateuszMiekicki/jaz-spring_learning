package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.DTO.SubcategoryDTO;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;

import java.util.List;

@RestController
public class SubcategoryController {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryController(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("allezon/categories/{category}")
    public ResponseEntity<List<SubcategoryEntity>> getSubcategory(@PathVariable("category") String category) {
        CategoryEntity categoryEntity = categoryRepository.findByName(category);
        if (categoryEntity == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(subcategoryRepository.getSubcategory(categoryEntity), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/categories/subcategories")
    public ResponseEntity<List<SubcategoryEntity>> addSubcategory(@RequestBody SubcategoryDTO subcategoryDTO) {
        if (categoryRepository.findByName(subcategoryDTO.getName()) != null) {
            return new ResponseEntity("Such an subcategory exists in the category.", HttpStatus.CONFLICT);
        }
        subcategoryRepository.addSubcategory(subcategoryDTO);
        return new ResponseEntity("Added subcategory", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/categories/subcategories")
    public ResponseEntity<List<SubcategoryEntity>> deleteSubcategory(@RequestBody SubcategoryDTO subcategoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findById(subcategoryDTO.getCategoryId());
        if (categoryRepository.findById(subcategoryDTO.getCategoryId()) == null) {
            return new ResponseEntity("No subcategories in the selected category.", HttpStatus.NOT_FOUND);
        }
        SubcategoryEntity subcategoryEntity = subcategoryRepository.findByIdCategoryAndNameSubcategory(categoryEntity.getId(), subcategoryDTO.getName());
        if (subcategoryEntity == null) {
            return new ResponseEntity("Such an subcategory exists in the category.", HttpStatus.CONFLICT);
        }
        subcategoryRepository.deleteSubcategory(subcategoryEntity);
        return new ResponseEntity("Added subcategory", HttpStatus.NO_CONTENT);
    }
}