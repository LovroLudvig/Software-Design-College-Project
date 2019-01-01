package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Style;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleRepository extends JpaRepository<Style, Long> {
}
