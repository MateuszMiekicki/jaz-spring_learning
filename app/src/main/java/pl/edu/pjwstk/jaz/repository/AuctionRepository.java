package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pjwstk.jaz.entity.AuctionEntity;
import pl.edu.pjwstk.jaz.entity.UserEntity;

public interface AuctionRepository extends CrudRepository<AuctionEntity, Long> {
    Iterable<? extends AuctionEntity> findAllByAuthor(UserEntity userEntity);
}
