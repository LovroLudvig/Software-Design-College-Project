package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {

    Town findByPostCode(int postCode);
}
