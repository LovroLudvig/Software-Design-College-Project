package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    OrderStatus findByName(String name);
}
