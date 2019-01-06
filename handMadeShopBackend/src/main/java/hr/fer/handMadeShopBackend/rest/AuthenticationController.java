package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.UserService;
import hr.fer.handMadeShopBackend.service.UserStatusService;
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

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        // The password hash property from the request is not actually a hash
        String hash = encoder.encode(user.getPasswordHash());

        user.setPasswordHash(hash);
        inMemoryUserDetailsManager.createUser(
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(hash).roles(Constants.ROLE_USER).build());

        return userService.save(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User login(@RequestBody User user) {
        if(user == null) {
            throw new IllegalArgumentException("The user with his username must be provided in body of the request.");
        }
        return userService.fetch(user.getUsername());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void logout() {}
}
