package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.ParameterEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;

@Transactional
@Repository
public class ParameterRepository {
    private final EntityManager entityManager;

    public ParameterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ParameterEntity getByName(String name) {
        try {
            ParameterEntity parameterEntity = entityManager.createQuery("select pe from ParameterEntity pe where pe.name=:name", ParameterEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return parameterEntity;
        } catch (NoResultException msg) {
            return null;
        }
    }

}
