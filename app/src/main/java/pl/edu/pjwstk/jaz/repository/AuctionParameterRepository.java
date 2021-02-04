package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pjwstk.jaz.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.entity.AuctionParameterEntity;

public interface AuctionParameterRepository extends CrudRepository<AuctionParameterEntity, Long> {
    Iterable<? extends AuctionParameterEntity> findAllByAuctionEntity(AuctionEntity auctionEntity);
}
