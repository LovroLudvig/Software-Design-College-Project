package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.Exceptions.NotFoundException;
import hr.fer.handMadeShopBackend.dao.StoryRepository;
import hr.fer.handMadeShopBackend.dao.StoryStatusRepository;
import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.domain.StoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryStatusRepository storyStatusRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Override
    public Story recommendStory(Story story) {
        validate(story);

        StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_IN_EVALUATION);
        story.setStatus(status);

        return storyRepository.save(story);
    }

    @Override
    public Story publishStory(Story story) {
        validate(story);

        StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_ALLOWED);
        story.setStatus(status);

        return storyRepository.save(story);
    }

    @Override
    public List<Story> listAllStories() {
        return storyRepository.findAll();
    }

    @Override
    public Story manageRecommendedStory(Long storyId, boolean isAllowed) {
        if(storyId == null) {
            throw new IllegalArgumentException("The id of the story to manage must be provided.");
        }
        Optional<Story> opt = storyRepository.findById(storyId);
        if(!opt.isPresent()) {
            throw new NotFoundException("The story with the specified id does not exist!");
        }
        Story story = opt.get();

        if(isAllowed) {
            StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_ALLOWED);
            story.setStatus(status);
            return storyRepository.save(story);
        } else {
            StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_DENIED);
            story.setStatus(status);
            storyRepository.delete(story);
            return null;
        }
    }

    private void validate(Story story) {
        if(story == null) {
            throw new IllegalArgumentException("The recommended story cannot be null");
        }
    }


}
