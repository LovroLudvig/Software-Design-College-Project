package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.domain.User;

import java.util.List;

public interface StoryService {

    Story recommendStory(Story story);
    Story fetch(Long id);
    Story save(Story story);
    Story publishStory(Story story);
    List<Story> listAllStories();
    List<Story> listMyRecommendedStories(User user);
    List<Story> listAllInEvaluation();
    Story setSeen(Story story);
    Story manageRecommendedStory(Long storyId, boolean isAllowed);
}
