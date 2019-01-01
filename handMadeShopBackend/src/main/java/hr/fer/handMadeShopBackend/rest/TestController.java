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
@RequestMapping("/hello")
@Lazy
public class TestController {

    @Autowired
    private UserService service;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @GetMapping("")
    public User helloPublic() {
        User a = service.fetch("almer101");
        return a;
    }

    @GetMapping("/exists/{username}")
    public boolean exists(@PathVariable("username") String username) {
        return inMemoryUserDetailsManager.userExists(username);
    }

    @GetMapping("/private")
    public User helloPrivate() {
        return new User();
    }

    @RequestMapping("add/{username}/{password}")
    @ResponseStatus(HttpStatus.CREATED)
    public String add(@PathVariable("username") String username, @PathVariable("password") String password) {
        String hash = passwordEncoder().encode(password);
        inMemoryUserDetailsManager.createUser(
                org.springframework.security.core.userdetails.User.withUsername(username)
                        .password(hash).roles("USER", "ADMIN").build());
        return "added";
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
