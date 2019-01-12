package hr.fer.handMadeShopBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.dao.CommentsRepository;
import hr.fer.handMadeShopBackend.dao.StoryRepository;
import hr.fer.handMadeShopBackend.domain.Comment;
import hr.fer.handMadeShopBackend.domain.Story;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentsRepository commentsRepo;
	
	@Autowired 
	private StoryRepository storyRepo;


	@Override
	public Comment postComment(Comment comment, Long storyId) {
		Comment comment2 = commentsRepo.save(comment);
		Story story = storyRepo.getOne(storyId);
		List<Comment> komentari = story.getComments();
		komentari.add(comment);
		story.setComments(komentari);
		storyRepo.save(story);
		return comment2;
	}

}
