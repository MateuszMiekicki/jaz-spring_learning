package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.AuctionDTO;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class AuctionRepository {
    private final EntityManager entityManager;

    public AuctionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<AuctionEntity> getAuctions() {
        return entityManager
                .createQuery("select ae from AuctionEntity ae", AuctionEntity.class)
                .getResultList();
    }

    public AuctionEntity addAuction(AuctionDTO auctionDTO) {
        AuctionEntity auctionEntity = new AuctionEntity();
        auctionEntity.setAuthorId(auctionDTO.getAuthorId());
        auctionEntity.setCategoryId(auctionDTO.getCategoryId());
        auctionEntity.setDescription(auctionDTO.getDescription());
        auctionEntity.setPrice(auctionDTO.getPrice());
        auctionEntity.setTitle(auctionDTO.getTitle());
        auctionEntity.setSubcategoryId(auctionDTO.getSubcategoryId());
        entityManager.persist(auctionEntity);
        return auctionEntity;
    }

    public void updateAuction(AuctionDTO auctionDTO){
        AuctionEntity auctionEntity = new AuctionEntity();
        auctionEntity.setId(auctionDTO.getAuctionId());
        auctionEntity.setAuthorId(auctionDTO.getAuthorId());
        auctionEntity.setCategoryId(auctionDTO.getCategoryId());
        auctionEntity.setDescription(auctionDTO.getDescription());
        auctionEntity.setPrice(auctionDTO.getPrice());
        auctionEntity.setTitle(auctionDTO.getTitle());
        auctionEntity.setSubcategoryId(auctionDTO.getSubcategoryId());
        entityManager.merge(auctionEntity);
    }
}
