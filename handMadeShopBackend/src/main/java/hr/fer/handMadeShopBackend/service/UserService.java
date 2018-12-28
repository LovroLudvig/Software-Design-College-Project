package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.User;

import java.util.Optional;

public interface UserService {

    User fetch(String username);
    User save(User user);
}
