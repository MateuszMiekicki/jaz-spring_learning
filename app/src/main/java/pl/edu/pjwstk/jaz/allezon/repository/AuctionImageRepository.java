package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionImageEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class AuctionImageRepository {
    private final EntityManager entityManager;

    public AuctionImageRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addImage(AuctionImageEntity imageEntity){
        entityManager.persist(imageEntity);
    }

    public List<AuctionImageEntity> getImages(Long auctionId){
        return entityManager.createQuery("select ie from AuctionImageEntity ie where ie.auctionId=:auctionId")
                .setParameter("auctionId", auctionId)
                .getResultList();
    }

    public void deleteImage(AuctionImageEntity auctionImageEntity){
        entityManager.remove(auctionImageEntity);
    }
}
