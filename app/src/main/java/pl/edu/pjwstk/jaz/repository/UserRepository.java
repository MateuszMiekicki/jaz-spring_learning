package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
