package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Lazy
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("/{username}")
    public User getUserWithUsername(@PathVariable("username") String username) {
        return userService.fetch(username);
    }

    @PostMapping("/delete/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User deleteUser(@PathVariable("username") String username) {
        inMemoryUserDetailsManager.deleteUser(username);
        return userService.deleteUser(username);
    }

    @PostMapping("/forbid/{username}")
    public void forbidAccessUser(@PathVariable("username") String username) {
        inMemoryUserDetailsManager.deleteUser(username);
        userService.forbidAccess(username);
    }

    @PostMapping("/update")
    public User updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
}
