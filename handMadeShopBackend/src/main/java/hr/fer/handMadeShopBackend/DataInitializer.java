package hr.fer.handMadeShopBackend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.domain.Advertisement;
import hr.fer.handMadeShopBackend.domain.Dimension;
import hr.fer.handMadeShopBackend.domain.Style;
import hr.fer.handMadeShopBackend.service.AdService;
import hr.fer.handMadeShopBackend.service.DimensionService;
import hr.fer.handMadeShopBackend.service.OrderStatusService;
import hr.fer.handMadeShopBackend.service.RoleService;
import hr.fer.handMadeShopBackend.service.StoryStatusService;
import hr.fer.handMadeShopBackend.service.StyleService;
import hr.fer.handMadeShopBackend.service.UserStatusService;

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
    
    @Autowired
    private AdService adService;
    
    @Autowired 
    private StyleService styleService;
    
    @Autowired
    private DimensionService dimensionService;

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
        
        createFakeAds("Šaht", "Bakrena žica", "Ljuska od jaja", "Ruska nevjesta", "Prazna kutija cigareta");
        
        
        //TODO dragi martine sad sam se sjetio da nisi osiguro adove od null ap sredi to kad si gotov
    }
    
    private void createFakeAds(String... names) {
    	for(String name: names) {
    		Advertisement ad1 = new Advertisement();
            ad1.setDescription("Some description");
            ad1.setName(name);
            ad1.setPrice(1000.0);
            ad1.setSpecification("Unspecified");
            ad1.setImageURL("https://cdn.shopify.com/s/files/1/0095/4332/t/30/assets/no-image.svg?3412253793887835423");
            
            List<Dimension> dimensions = new ArrayList<Dimension>();
            Dimension dim1 = new Dimension();
            Dimension dim2 = new Dimension();
            dim1.setDescription("n/a");
            dim2.setDescription("n/a");
            dimensions.add(dim1);
            dimensions.add(dim2);
            dimensionService.save(dim1);
            dimensionService.save(dim2);
            ad1.setDimensions(dimensions);
            
            List<Style> styles = new ArrayList<Style>();
            Style stil1 = new Style();
            Style stil2 = new Style();
            stil1.setDescription("Jonski");
            stil1.setPrice(100.0);
            stil2.setDescription("Antistil");
            stil2.setPrice(150.0);
            styleService.save(stil1);
            styleService.save(stil2);
            styles.add(stil1);
            styles.add(stil2);
            ad1.setStyles(styles);
            
            adService.save(ad1);
    	}
    }
    

}

