package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.Story;

import java.util.List;

public interface StoryService {

    Story recommendStory(Story story);
    Story publishStory(Story story);
    List<Story> listAllStories();
    Story manageRecommendedStory(Long storyId, boolean isAllowed);
}
