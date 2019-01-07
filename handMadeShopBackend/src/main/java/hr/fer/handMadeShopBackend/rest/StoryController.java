package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.domain.Story;
import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stories")
@Lazy
public class StoryController {

    @Autowired
    private StoryService storyService;

    @PostMapping("/recommend")
    public Story recommendStory(@RequestBody Story story) {
        return storyService.recommendStory(story);
    }

    @PostMapping("/publish")
    public Story publishStory(@RequestBody Story story) {
        return storyService.publishStory(story);
    }

    @GetMapping("/all")
    public List<Story> listAllStories() {
        return storyService.listAllStories();
    }

    @PostMapping("/myrecommended")
    public List<Story> listMyRecommendedStories(@RequestBody User user) {
        return storyService.listMyRecommendedStories(user);
    }

    @PostMapping("/setseen/{storyId}")
    public Story setSeen(@PathVariable("storyId") long storyId) {
        Story story = storyService.fetch(storyId);
        return storyService.setSeen(story);
    }

    @PostMapping("/manage")
    public Story manageRecommendedStory(@RequestParam(value="storyId", required=true) Long storyId,
                                        @RequestParam(value="isAllowed", required=true) boolean isAllowed) {
        return storyService.manageRecommendedStory(storyId, isAllowed);
    }

    @GetMapping("/evaluation")
    public List<Story> unmanagedStories() {
        return storyService.listAllInEvaluation();
    }
}
