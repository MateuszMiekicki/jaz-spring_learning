package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionImageDTO;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionImageEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
@Repository
public class AuctionImageRepository {
    private final EntityManager entityManager;

    public AuctionImageRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addImage(AuctionImageDTO imageDTO){
        AuctionImageEntity imageEntity = new AuctionImageEntity();
        imageEntity.setSectionId(imageDTO.getAuctionId());
        imageEntity.setUrl(imageDTO.getURL());
        entityManager.persist(imageEntity);
    }
}
