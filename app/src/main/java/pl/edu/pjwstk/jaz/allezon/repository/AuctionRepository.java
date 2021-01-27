package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class AuctionRepository {
    private final EntityManager entityManager;
    private Long lastId;

    public AuctionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<AuctionEntity> getAuctions() {
        return entityManager
                .createQuery("select ae from AuctionEntity ae", AuctionEntity.class)
                .getResultList();
    }

    public void addAuction(AuctionEntity auctionEntity) {
        entityManager.persist(auctionEntity);
        setLastId(auctionEntity.getId());
    }

    public void updateAuction(AuctionEntity auctionEntity) {
        entityManager.merge(auctionEntity);
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public AuctionEntity findByAuthorIdAndAuctionId(Long authorId, Long auctionId) {
        try {
            AuctionEntity auctionEntity = entityManager
                    .createQuery("select ae from AuctionEntity ae where ae.authorId=:authorId and ae.id=:auctionId", AuctionEntity.class)
                    .setParameter("authorId", authorId)
                    .setParameter("auctionId", auctionId)
                    .getSingleResult();
            return auctionEntity;
        } catch (NoResultException msg) {
            return null;
        }

    }

    public void deleteAuction(AuctionEntity auctionEntity) {
        entityManager.remove(auctionEntity);
    }
}
