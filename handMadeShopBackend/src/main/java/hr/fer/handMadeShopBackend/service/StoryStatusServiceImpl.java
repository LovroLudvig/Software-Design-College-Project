package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.dao.StoryStatusRepository;
import hr.fer.handMadeShopBackend.domain.StoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryStatusServiceImpl implements StoryStatusService {

    @Autowired
    private StoryStatusRepository storyStatusRepository;

    @Override
    public StoryStatus findByName(String name) {
        return storyStatusRepository.findByName(name);
    }

    @Override
    public StoryStatus saveStoryStatusWithName(String name) {
        StoryStatus status = new StoryStatus();
        status.setName(name);

        return storyStatusRepository.save(status);
    }
}
