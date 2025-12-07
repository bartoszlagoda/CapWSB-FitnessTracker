package pl.wsb.fitnesstracker.user.internal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.wsb.fitnesstracker.user.api.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
class UserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findUsersByName(String name) {
        String jpql = "SELECT u FROM User u WHERE u.name = :name";

        return entityManager.createQuery(jpql, User.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
}