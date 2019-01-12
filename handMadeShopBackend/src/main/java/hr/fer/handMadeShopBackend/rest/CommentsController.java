package hr.fer.handMadeShopBackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.handMadeShopBackend.domain.Comment;
import hr.fer.handMadeShopBackend.service.CommentService;
import hr.fer.handMadeShopBackend.service.StoryService;

@RestController
@RequestMapping("/comments")
@Lazy
public class CommentsController {
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/post/{storyId}")
    @ResponseStatus(HttpStatus.OK)
    public Comment register(@PathVariable("storyId") Long storyId, @RequestBody Comment comment) {
        return commentService.postComment(comment, storyId);
    }

}


