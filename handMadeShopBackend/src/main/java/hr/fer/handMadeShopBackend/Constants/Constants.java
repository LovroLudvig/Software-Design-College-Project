package hr.fer.handMadeShopBackend.Constants;

public class Constants {

    // Roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    // User status constants
    public static final String USER_STATUS_ALLOWED = "ACCESS_ALLOWED";
    public static final String USER_STATUS_FORBIDDEN = "ACCESS_FORBIDDEN";

    // Story status constants
    public static final String STORY_STATUS_ALLOWED = "STORY_ALLOWED";
    public static final String STORY_STATUS_ALLOWED_SEEN = "STORY_ALLOWED_SEEN";
    public static final String STORY_STATUS_DENIED = "STORY_DENIED";
    public static final String STORY_STATUS_IN_EVALUATION = "STORY_IN_EVALUATION";

    // Order status constants
    public static final String ORDER_STATUS_ALLOWED = "ORDER_ALLOWED";
    public static final String ORDER_STATUS_DENIED = "ORDER_DENIED";
    public static final String ORDER_STATUS_IN_EVALUATION = "ORDER_IN_EVALUATION";

    // Image locations
    public static final String IMAGE_BASE_URL_STORIES = "src/main/resources/images/stories/";
    public static final String IMAGE_BASE_URL_ADVERTISEMENTS = "src/main/resources/images/advertisements/";

    // Video locations
    public static final String VIDEO_BASE_URL_STORIES = "src/main/resources/videos/stories/";
}
