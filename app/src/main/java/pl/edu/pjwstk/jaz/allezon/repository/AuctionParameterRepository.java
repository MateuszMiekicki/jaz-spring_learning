package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
@Repository
public class AuctionParameterRepository {
    private final EntityManager entityManager;

    public AuctionParameterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
