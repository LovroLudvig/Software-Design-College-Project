package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    int countByUsername(String username);
}
