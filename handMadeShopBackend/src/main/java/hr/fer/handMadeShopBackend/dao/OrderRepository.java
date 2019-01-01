package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.AdOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<AdOrder, Long> {

}
