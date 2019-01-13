package hr.fer.handMadeShopBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.handMadeShopBackend.domain.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
