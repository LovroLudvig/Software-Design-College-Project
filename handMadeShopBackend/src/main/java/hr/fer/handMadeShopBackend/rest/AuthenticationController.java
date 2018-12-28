package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Lazy
public class AuthenticationController {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    private UserService userService;

//    @Autowired
//    public void SimpleSecurityController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
//        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
//    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        // The password hash property from the request is not actually a hash
        User u = userService.fetch(user.getUsername());

        String hash = encoder.encode(user.getPasswordHash());
        user.setPasswordHash(hash);
        inMemoryUserDetailsManager.createUser(
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(hash).roles(USER, ADMIN).build());
        return userService.save(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void login() {}

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void logout() {}
}
