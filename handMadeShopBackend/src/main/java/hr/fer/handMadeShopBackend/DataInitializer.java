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
        createFakeAds();
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

    private void createFakeAds() {
        String[] descriptions = new String[] {
            "This is one of the types of manhole you can possibly find on todays market. You can find all possible dimension below.",
            "The best possible deal for various copper wires and copper products.",
            "Premium egg shells. Gotten from organic farms in Norway.",
            "Mail order bride from egzotic Russia. For more details add me on snapchat.",
            "Empty cigarette box. Only for collectors."
        };

        String[] imageUrls = new String[] {
            "https://hiddencityphila.org/wp-content/uploads/2018/01/PECO-manhole-cover-3.jpg",
            "https://5.imimg.com/data5/VQ/NH/MY-2555326/magnet-copper-wire-500x500.jpg",
            "https://i1.wp.com/thecostaricanews.com/wp-content/uploads/2017/09/eggshells.jpg?fit=1200%2C1550&ssl=1",
            "https://static.standard.co.uk/s3fs-public/thumbnails/image/2018/08/28/11/upsetbride2808a.jpg?w968",
            "https://www.iconexperience.com/_img/v_collection_png/256x256/shadow/cigarette_packet_empty.png"
        };

        String[] adNames = new String[] {
            "Manhole",
            "Copper wire - 10 m",
            "Premium egg shells - 12 pieces",
            "Russian bride",
            "Empty cigarrete box"
        };

        double[] prices = new double[] { 39.99, 14.99, 2.99, 999.99, 1.99 };

        String[] specifications = new String[] {
            "Tool used for sewer opennings",
            "Electricity conductor",
            "Healthy fertlizer",
            "Good life, good wife",
            "Garbage"
        };

        String[] dimensionNames = new String[] {
            "big", "bigger",
            "child size",
            "bag size",
            "medium"
        };

        String[] styleNames = new String[] {
            "Jonski",
            "Antistil"
        };

    	for(int i = 0; i < descriptions.length; i++) {
    		Advertisement ad1 = new Advertisement();
            ad1.setDescription(descriptions[i]);
            ad1.setName(adNames[i]);
            ad1.setPrice(prices[i]);
            ad1.setSpecification(specifications[i]);
            ad1.setImageURL(imageUrls[i]);
            
            List<Dimension> dimensions = new ArrayList<Dimension>();
            for(String d : dimensionNames) {
                Dimension dim = new Dimension();
                dim.setDescription(d);
                dimensions.add(dim);
                dimensionService.save(dim);
            }
            ad1.setDimensions(dimensions);
            
            List<Style> styles = new ArrayList<Style>();
            for(String styleName : styleNames) {
                Style style = new Style();
                style.setDescription(styleName);
                boolean less = Math.random() < 0.5;
                style.setPrice(less ? 100.0 : 150.0);
                styleService.save(style);
                styles.add(style);
            }
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

