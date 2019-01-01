package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
