package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.Comment;

public interface CommentService {
	Comment postComment(Comment comment, Long storyId);
}
