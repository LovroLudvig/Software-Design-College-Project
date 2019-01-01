package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionRepository extends JpaRepository<Dimension, Long> {
}
