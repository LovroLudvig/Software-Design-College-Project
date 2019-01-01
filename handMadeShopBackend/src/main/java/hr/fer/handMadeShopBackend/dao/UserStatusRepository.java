package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

    UserStatus findByName(String name);
}
