package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    int countByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
