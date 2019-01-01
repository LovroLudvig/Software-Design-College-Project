package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.StoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryStatusRepository extends JpaRepository<StoryStatus, Long> {

    StoryStatus findByName(String name);
}
