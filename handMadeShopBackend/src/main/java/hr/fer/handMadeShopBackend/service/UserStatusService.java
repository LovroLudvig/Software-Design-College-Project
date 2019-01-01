package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.UserStatus;

public interface UserStatusService {

    public UserStatus findByName(String name);
    public UserStatus saveStatusWithName(String name);
}
