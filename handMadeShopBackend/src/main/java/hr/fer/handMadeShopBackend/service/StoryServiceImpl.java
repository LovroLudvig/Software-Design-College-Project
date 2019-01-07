package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.Exceptions.NotFoundException;
import hr.fer.handMadeShopBackend.dao.StoryRepository;
import hr.fer.handMadeShopBackend.dao.StoryStatusRepository;
import hr.fer.handMadeShopBackend.dao.UserRepository;
import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.domain.StoryStatus;
import hr.fer.handMadeShopBackend.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryStatusRepository storyStatusRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Story recommendStory(Story story) {
        validate(story);

        User user = story.getUser();
        if(user == null) {
            throw new IllegalArgumentException("The user which recommended the story must not be null.");
        }
        user = userRepository.findByUsername(user.getUsername());
        story.setUser(user);

        StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_IN_EVALUATION);
        story.setStatus(status);

        return storyRepository.save(story);
    }

    @Override
    public Story fetch(Long id) {
        Optional<Story> opt = storyRepository.findById(id);
        return opt.isPresent() ? opt.get() : null;
    }

    @Override
    public Story save(Story story) {
        if(story == null) {
            throw new IllegalArgumentException("The story to save must not be null");
        }
        return storyRepository.save(story);
    }

    @Override
    public Story publishStory(Story story) {
        validate(story);

        User user = story.getUser();
        if(user == null) {
            throw new IllegalArgumentException("The user which published the story must not be null.");
        }
        user = userRepository.findByUsername(user.getUsername());
        story.setUser(user);

        StoryStatus status = storyStatusRepository.findByName(Constants.STORY_STATUS_ALLOWED);
        story.setStatus(status);

        return storyRepository.save(story);
    }

    @Override
    public List<Story> listAllStories() {
        return storyRepository.findAll()
                .stream()
                .filter(s -> s.getStatus().getName().equals(Constants.STORY_STATUS_ALLOWED) ||
                            s.getStatus().getName().equals(Constants.STORY_STATUS_ALLOWED_SEEN))
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> listMyRecommendedStories(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        return storyRepository.findAll()
                .stream()
                .filter(s -> s.getUser().getUsername().equals(user.getUsername()))
                .filter(s -> s.getStatus().getName().equals(Constants.STORY_STATUS_IN_EVALUATION) ||
                        s.getStatus().getName().equals(Constants.STORY_STATUS_ALLOWED))
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> listAllInEvaluation() {
        return storyRepository.findAll()
                .stream()
                .filter(s -> s.getStatus().getName().equals(Constants.STORY_STATUS_IN_EVALUATION))
                .collect(Collectors.toList());
    }

    @Override
    public Story setSeen(Story story) {
        if(story == null) {
            throw new IllegalArgumentException("Story can not be null");
        }
        StoryStatus status = storyStatusRepository.findByName(Constants.ORDER_STATUS_ALLOWED_SEEN);
        story.setStatus(status);
        return storyRepository.save(story);
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
