package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pjwstk.jaz.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.entity.AuctionImageEntity;

public interface AuctionImageRepository extends CrudRepository<AuctionImageEntity, Long> {
    Iterable<? extends AuctionImageEntity> findAllByAuctionEntity(AuctionEntity auctionEntity);
}
