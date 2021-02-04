package pl.edu.pjwstk.jaz.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pjwstk.jaz.entity.ParameterEntity;

public interface ParameterRepository extends CrudRepository<ParameterEntity, Long> {
    ParameterEntity findByName(String name);
}
