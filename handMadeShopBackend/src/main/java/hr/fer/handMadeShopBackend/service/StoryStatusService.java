package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.StoryStatus;

public interface StoryStatusService {

    StoryStatus findByName(String name);
    StoryStatus saveStoryStatusWithName(String name);
}
