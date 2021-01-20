package pl.edu.pjwstk.jaz.allezon.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.DTO.UserDTO;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

//https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
@Transactional
@Service
public class RegisterService {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RegisterService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        Integer userRoleId = 2;
        userEntity.setRole_id(userRoleId);
        entityManager.persist(userEntity);
    }

    public boolean emailExists(String email) {
        try {
            entityManager.createQuery("select ue from UserEntity ue where ue.email=:email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return true;
        } catch (NoResultException msg) {
            return false;
        }
    }
}
