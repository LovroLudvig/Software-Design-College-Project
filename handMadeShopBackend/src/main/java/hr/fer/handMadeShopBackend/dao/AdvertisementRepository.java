package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
