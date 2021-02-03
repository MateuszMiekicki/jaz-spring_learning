package pl.edu.pjwstk.jaz.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.DTO.CategoryDTO;
import pl.edu.pjwstk.jaz.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.repository.SubcategoryRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CategoryRestController {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public CategoryRestController(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @GetMapping("allezon/categories")
    public void getCategories(HttpServletResponse response) throws IOException {
        JSONObject categoriesJSON = new JSONObject();
        for (CategoryEntity categoryEntity : categoryRepository.findAll()) {
            JSONArray subcategoriesJSON = new JSONArray();
            for (SubcategoryEntity subcategoryEntity : categoryEntity.getSubcategories()) {
                subcategoriesJSON.put(subcategoryEntity.getName());
            }
            categoriesJSON.put(categoryEntity.getName(), subcategoriesJSON);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(categoriesJSON.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("allezon/categories")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        if(categoryDTO.getName().isEmpty()){
            return new ResponseEntity<>("Category name is empty.", HttpStatus.BAD_REQUEST);
        }
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            return new ResponseEntity<>("Such an category exists in the database.", HttpStatus.CONFLICT);
        }
        categoryRepository.save(new CategoryEntity().withName(categoryDTO.getName()));
        return new ResponseEntity<>("Added category.", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("allezon/categories")
    public ResponseEntity<String> deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        if(categoryDTO.getName().isEmpty()){
            return new ResponseEntity<>("Category name is empty.", HttpStatus.BAD_REQUEST);
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryDTO.getName());
        if (categoryEntity == null) {
            return new ResponseEntity<>("Such an category does not exists in the database.", HttpStatus.NOT_FOUND);
        }
        for(SubcategoryEntity subcategoryEntity : categoryEntity.getSubcategories()){
            subcategoryRepository.delete(subcategoryEntity);
        }
        categoryRepository.delete(categoryEntity);
        return new ResponseEntity<>("Deleted category.", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("allezon/categories")
    public ResponseEntity<String> editCategory(@RequestBody CategoryDTO categoryDTO) {
        if(categoryDTO.getName().isEmpty() || categoryDTO.getNewName().isEmpty()){
            return new ResponseEntity<>("Category name or new category name is empty.", HttpStatus.BAD_REQUEST);
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryDTO.getName());
        if (categoryEntity == null) {
            return new ResponseEntity<>("Such an category does not exists in the database.", HttpStatus.NOT_FOUND);
        }
        categoryEntity.setName(categoryDTO.getNewName());
        categoryRepository.save(categoryEntity);
        return new ResponseEntity<>("Updated category.", HttpStatus.OK);
    }
}