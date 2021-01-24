package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.SectionDTO;
import pl.edu.pjwstk.jaz.allezon.entity.SectionEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class SectionRepository {
    private final EntityManager entityManager;

    public SectionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addSection(SectionDTO sectionDTO) {
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setName(sectionDTO.getName());
        entityManager.persist(sectionEntity);
    }

    public void deleteSection(SectionEntity sectionEntity) {
        entityManager.remove(sectionEntity);
    }

    public SectionEntity findByName(String name) {
        try {
            SectionEntity sectionEntity = entityManager
                    .createQuery("select se from SectionEntity se where se.name=:name", SectionEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return sectionEntity;
        } catch (NoResultException msg) {
            return null;
        }
    }

    public List<SectionEntity> getSections() {
        return entityManager
                .createQuery("select se from SectionEntity se", SectionEntity.class)
                .getResultList();
    }
}
