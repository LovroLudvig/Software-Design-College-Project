package hr.fer.handMadeShopBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.Exceptions.NotFoundException;
import hr.fer.handMadeShopBackend.dao.CommentsRepository;
import hr.fer.handMadeShopBackend.dao.StoryRepository;
import hr.fer.handMadeShopBackend.dao.UserRepository;
import hr.fer.handMadeShopBackend.domain.Comment;
import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.domain.User;
import net.bytebuddy.implementation.bytecode.Throw;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentsRepository commentsRepo;
	
	@Autowired 
	private StoryRepository storyRepo;
	
	 @Autowired
	 private UserRepository userRepo;



	@Override
	public Comment postComment(Comment comment, String username, Long storyId) {
		User user = null;
		if( username != null) {
			user = userRepo.findByUsername(username);
			if (user == null) {
				throw new NotFoundException("Cannot find user with provided username");
			}
		}
		comment.setUser(user);
		comment = commentsRepo.save(comment);

		Story story = storyRepo.getOne(storyId);

		List<Comment> commentsList = story.getComments();
		commentsList.add(comment);
		story.setComments(commentsList);
		storyRepo.save(story);

		return comment;
	}

}
