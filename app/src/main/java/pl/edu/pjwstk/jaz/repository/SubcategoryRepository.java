package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pjwstk.jaz.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.entity.SubcategoryEntity;

import java.util.List;

public interface SubcategoryRepository extends CrudRepository<SubcategoryEntity, Long> {
    List<SubcategoryEntity> findByCategoryEntity(CategoryEntity categoryEntity);
    SubcategoryEntity findByCategoryEntityAndName(CategoryEntity categoryEntity, String subcategoryName);
}
