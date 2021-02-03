package pl.edu.pjwstk.jaz.controller;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.DTO.SubcategoryDTO;
import pl.edu.pjwstk.jaz.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.repository.SubcategoryRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;

@RestController
public class SubcategoryRestController {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public SubcategoryRestController(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @GetMapping("allezon/categories/{category}")
    public void getSubcategories(@PathVariable("category") String category, HttpServletResponse response) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findByName(category);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (categoryEntity == null) {
            response.getWriter().write("Such an category does not exists in the database.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        JSONArray subcategoriesJSON = new JSONArray();
        for (SubcategoryEntity subcategoryEntity : categoryEntity.getSubcategories()) {
            subcategoriesJSON.put(subcategoryEntity.getName());
        }

        response.getWriter().write(subcategoriesJSON.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("allezon/categories/subcategories")
    public ResponseEntity<String> addSubcategories(@RequestBody SubcategoryDTO subcategoryDTO) {
        if (subcategoryDTO.getCategoryName().isEmpty() || subcategoryDTO.getSubcategoryName().isEmpty()) {
            return new ResponseEntity<>("Field is empty.", HttpStatus.BAD_REQUEST);
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(subcategoryDTO.getCategoryName());
        if (categoryEntity != null) {
            Predicate<String> containsSubcategory = subcategoryName -> {
                for (SubcategoryEntity subcategoryEntity : subcategoryRepository.findByCategoryEntity(categoryEntity)) {
                    if (subcategoryEntity.getName().equals(subcategoryName)) {
                        return true;
                    }
                }
                return false;
            };
            if (!containsSubcategory.test(subcategoryDTO.getSubcategoryName())) {
                subcategoryRepository.save(new SubcategoryEntity()
                        .withName(subcategoryDTO.getSubcategoryName())
                        .withCategoryEntity(categoryEntity));
                return new ResponseEntity<>("Added subcategory.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("A subcategory in this category already exists.", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Category does not exists.", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("allezon/categories/subcategories")
    public ResponseEntity<String> deleteSubcategories(@RequestBody SubcategoryDTO subcategoryDTO) {
        if (subcategoryDTO.getCategoryName().isEmpty() || subcategoryDTO.getSubcategoryName().isEmpty()) {
            return new ResponseEntity<>("Field is empty.", HttpStatus.BAD_REQUEST);
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(subcategoryDTO.getCategoryName());
        if (categoryEntity != null) {
            SubcategoryEntity subcategoryEntity = subcategoryRepository.findByCategoryEntityAndName(categoryEntity, subcategoryDTO.getSubcategoryName());
            if (subcategoryEntity != null) {
                subcategoryRepository.delete(subcategoryEntity);
                return new ResponseEntity<>("Subcategory deleted.", HttpStatus.NO_CONTENT);

            } else {
                return new ResponseEntity<>("Subcategory does not exists.", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Category does not exists.", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("allezon/categories/subcategories")
    public ResponseEntity<String> editSubcategories(@RequestBody SubcategoryDTO subcategoryDTO) {
        if (subcategoryDTO.getCategoryName().isEmpty() || subcategoryDTO.getSubcategoryName().isEmpty() || subcategoryDTO.getNewSubcategoryName().isEmpty()) {
            return new ResponseEntity<>("Field is empty.", HttpStatus.BAD_REQUEST);
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(subcategoryDTO.getCategoryName());
        if (categoryEntity != null) {
            SubcategoryEntity subcategoryEntity = subcategoryRepository.findByCategoryEntityAndName(categoryEntity, subcategoryDTO.getSubcategoryName());
            if (subcategoryEntity != null) {
                subcategoryEntity.setName(subcategoryDTO.getNewSubcategoryName());
                subcategoryRepository.save(subcategoryEntity);
                return new ResponseEntity<>("Subcategory edited.", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Subcategory does not exists.", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Category does not exists.", HttpStatus.NOT_FOUND);
    }
}
