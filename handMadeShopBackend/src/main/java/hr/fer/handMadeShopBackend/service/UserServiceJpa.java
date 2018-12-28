package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.dao.UserRepository;
import hr.fer.handMadeShopBackend.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceJpa implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User fetch(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(User user) {
        if(userRepo.countByUsername(user.getUsername()) != 0) {
            throw new IllegalArgumentException("The user should not exist");
        }
        return userRepo.save(user);
    }
}
