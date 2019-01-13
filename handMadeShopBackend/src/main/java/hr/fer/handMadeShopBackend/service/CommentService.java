package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.Comment;

import java.util.List;

public interface CommentService {
	List<Comment> postComment(Comment comment, String username, Long storyId);
}
