package hr.fer.handMadeShopBackend;

import java.util.ArrayList;
import java.util.List;

import hr.fer.handMadeShopBackend.domain.*;
import hr.fer.handMadeShopBackend.rest.AuthenticationController;
import hr.fer.handMadeShopBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import hr.fer.handMadeShopBackend.Constants.Constants;

/**
 * Example component used to insert some test students at application startup.
 */
@Component
@Lazy
public class DataInitializer {

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StoryStatusService storyStatusService;

    @Autowired
    private StoryService storyService;

    @Autowired
    private OrderStatusService orderStatusService;
    
    @Autowired
    private AdService adService;
    
    @Autowired 
    private StyleService styleService;
    
    @Autowired
    private DimensionService dimensionService;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

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
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_ALLOWED_SEEN);
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_DENIED);
        storyStatusService.saveStoryStatusWithName(Constants.STORY_STATUS_IN_EVALUATION);

        // Order status initialization
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_ALLOWED);
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_DENIED);
        orderStatusService.saveOrderStatusWithName(Constants.ORDER_STATUS_IN_EVALUATION);

        createUsers();
        createFakeAds("Šaht", "Bakrena žica", "Ljuska od jaja", "Ruska nevjesta", "Prazna kutija cigareta");
        createStories();


        //TODO dragi martine sad sam se sjetio da nisi osiguro adove od null ap sredi to kad si gotov
    }

    private void createUsers() {
        String[] usernames = new String[] {"mcolja", "ialmer", "ilovrencic", "lludvig", "hspolador", "mdadanovic"};
        String password = "secret";

        for(String username: usernames) {
            User user = new User();
            user.setUsername(username);
            String hash = encoder.encode(password);
            user.setPasswordHash(hash);

            inMemoryUserDetailsManager.createUser(
                    org.springframework.security.core.userdetails.User.withUsername(username)
                            .password(hash).roles(Constants.ROLE_ADMIN, Constants.ROLE_USER).build());

            userService.saveAdminUser(user);
        }
    }

    private void createFakeAds(String... names) {
    	for(String name: names) {
    		Advertisement ad1 = new Advertisement();
            ad1.setDescription("Some description for " + name);
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

    private void createStories() {
        String[] imageUrls = new String[]{"https://i.ytimg.com/vi/dkNCQeN45So/maxresdefault.jpg",
                "https://akos.ba/wp-content/uploads/2017/02/jorgovan-naslovna.jpg",
                "https://cenazlata.org/wp-content/uploads/2017/01/Otkup-folija-od-cigareta-istina-ili-zabluda-%C5%A1ta-treba-znati.jpg",
                "https://images-na.ssl-images-amazon.com/images/I/71dz1ztYLUL._SX425_.jpg",
                null};

        String[] storyTexts = new String[] {"My new napkin is crazy cool!",
                "Please check out my beautiful flowers I just bought yesterday. Please leave a like and subscribe.",
                "I am extreamely unhappy with this empty box of cigaretts I just bought! Not recommended!",
                "Quite unhappy with this Egg shells I recently bought. I was under impression I would only get the shells!!!",
                "Some text for the story"};

        String[] usernames = new String[]{"lludvig", "ilovrencic", "mcolja", "ilovrencic", "ialmer"};

        for(int i = 0; i < imageUrls.length; i++) {
            Story story = new Story();
            story.setImageUrl(imageUrls[i]);
            story.setText(storyTexts[i]);

            User user = new User();
            user.setUsername(usernames[i]);

            story.setUser(user);

            storyService.publishStory(story);
        }

    }
    

}

