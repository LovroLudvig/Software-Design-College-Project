package hr.fer.handMadeShopBackend.dao;

import hr.fer.handMadeShopBackend.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
