package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.User;

public interface UserService {

    User fetch(String username);
    User save(User user);
    User saveAdminUser(User user);
    User deleteUser(String username);
    User forbidAccess(String username);
    User updateUserInfo(User user);
}
