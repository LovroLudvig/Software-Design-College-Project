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

    }

    private void createUsers() {
        String[] usernames = new String[] {"mcolja", "ialmer", "ilovrencic", "lludvig", "hspolador", "mdadanovic", "istancin"};
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
            "Premium chair made from highest quality scandinavian oak. The chair is featured in several dimensions so whether you are skinny or a burger-eating champion, we have a size that is just for you.",
            "This vase is perfect high class decoration you can have in your home, if you are a vase enthusiast or just have a good taste, you can feel this vase calling out your name.",
            "Not many picture frames can highlight a story as well as this one. Be it the emotion of a familly christmas or the thrill of diving between the Iceland plates, your expirience is best remebered here.",
            "In this bowl of fruit even a grapefruit becomes sweet, get yours today!",
            "The quality of this carpet would even make Alladin jeleous. Although it can't fly it certantly looks like it appeared from a fairy tale.",
            "This large basket will help you carry whatever you need to wherever you need it",
            "Ordinary glass carefully decorated with love and transformed into art that feels like it's really yours and made for you."
        };

        String[] imageUrls = new String[] {
            "https://images.unsplash.com/photo-1494281232141-90a40b0b06c9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1529136490842-e2da7a4c7b74?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1519916478825-b1d7aef08f54?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1452457005517-a0dd81caca2a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=986&q=80",
            "https://images.unsplash.com/photo-1444362408440-274ecb6fc730?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1481061730414-e888962bd2c0?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=633&q=80",
            "https://images.unsplash.com/photo-1527838016968-2191bb805fc1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
        };

        String[] adNames = new String[] {
            "Chair",
            "Vase",
            "Picture Frame",
            "Fruit bowl",
            "Carpet",
            "Basket",
            "Ordinary glass??"
        };

        double[] prices = new double[] { 20.99, 60.99, 12.99, 15.99, 464.99, 739.99, 5.49 };

        String[] specifications = new String[] {
            "The latest in sitting techonology",
            "Vase for various uses",
            "Highlight your favourite moments",
            "Display your fruit",
            "A carpet to keep your house warm",
            "Carry your things with ease",
            "Regular drinking glass??"
        };

        String[] dimensionNames = new String[] {
        	"X-Large",
        	"Large", 
        	"Medium",
            "Small",
            "X-Small"
        };

        String[] styleNames = new String[] {
            "Ming",
            "Antique",
            "Modern",
            "Futuristic",
            "Traditional",
            "1920s",
            "1950s",
            "1960s",
            "1970s",
            "1980s",
            "1990s"
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
        String[] imageUrls = new String[]{"https://images.unsplash.com/photo-1496397604916-a3d9461942b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
                "https://cdn.pixabay.com/photo/2015/01/05/18/15/woman-589508_960_720.jpg",
                "https://images.unsplash.com/photo-1496360166961-10a51d5f367a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/uploads/1413170261747296531bd/f3aeb6f5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60",
                "https://images.unsplash.com/photo-1497296690583-da0e2a4ce49a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
                };

        String[] storyTexts = new String[] {"Just bought this super ordinary glass, nothing special about it!! ;)",
                "This is a grate basket, it has a LOT of room, now i can bring cake to my grandma.",
                "This ordinary glass is really cool, i like how it feels in my home!",
                "My carpet just flew in and it's really warm and fuzzy. Fast delivery and great quality, I'm super satisfied.",
                "This frame is just perfect for photos of my crazy adventures, thank you Craftery!",
               	};

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

