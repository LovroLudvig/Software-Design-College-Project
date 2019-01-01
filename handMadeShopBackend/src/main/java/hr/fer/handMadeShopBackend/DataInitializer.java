package hr.fer.handMadeShopBackend;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Example component used to insert some test students at application startup.
 */
@Component
public class DataInitializer {

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StoryStatusService storyStatusService;

    @Autowired
    private OrderStatusService orderStatusService;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        // User status initialization
        userStatusService.saveStatusWithName(Constants.USER_STATUS_ALLOWED);
        userStatusService.saveStatusWithName(Constants.USER_STATUS_FORBIDDEN);

        // Roles initialization
        roleService.saveRoleWithName(Constants.ROLE_USER);
        roleService.saveRoleWithName(Constants.ROLE_ADMIN);

        // Story status initialization
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_ALLOWED);
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_DENIED);
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_IN_EVALUATION);

        // Order status initialization
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_ALLOWED);
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_DENIED);
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_IN_EVALUATION);
    }

}

