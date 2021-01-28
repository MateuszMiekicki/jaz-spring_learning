package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionParameterEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class AuctionParameterRepository {
    private final EntityManager entityManager;

    public AuctionParameterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(AuctionParameterEntity auctionParameterEntity) {
        entityManager.persist(auctionParameterEntity);
    }

    public List<AuctionParameterEntity> findByAuctionId(Long id) {
        return entityManager
                .createQuery("select ae from AuctionParameterEntity ae where ae.auctionId=:id", AuctionParameterEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    public void delete(AuctionParameterEntity auctionParameterEntity) {
        entityManager.remove(auctionParameterEntity);
    }

    public void update(AuctionParameterEntity auctionParameterEntity) {
        entityManager.merge(auctionParameterEntity);
    }
}