package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.SubcategoryDTO;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class SubcategoryRepository {
    private final EntityManager entityManager;

    public SubcategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addSubcategory(SubcategoryDTO subcategoryDTO) {
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setCategoryId(subcategoryDTO.getCategoryId());
        subcategoryEntity.setName(subcategoryDTO.getName());
        entityManager.persist(subcategoryEntity);
    }

    public void deleteSubcategory(SubcategoryEntity subcategoryEntity) {
        entityManager.remove(subcategoryEntity);
    }

    public SubcategoryEntity findByNameAndId(Long categoryId, String name) {
        try {
            SubcategoryEntity subcategoryEntity = entityManager
                    .createQuery("select se from SubcategoryEntity se where se.name=:name and se.categoryId=:categoryId", SubcategoryEntity.class)
                    .setParameter("name", name)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
            return subcategoryEntity;
        } catch (NoResultException msg) {
            return null;
        }
    }

    public List<SubcategoryEntity> getSubcategory(CategoryEntity categoryEntity) {
        return entityManager
                .createQuery("select se from SubcategoryEntity se where se.categoryId=:id", SubcategoryEntity.class)
                .setParameter("id", categoryEntity.getId())
                .getResultList();
    }
}
