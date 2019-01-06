package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.dao.UserStatusRepository;
import hr.fer.handMadeShopBackend.domain.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepo;

    @Override
    public UserStatus findByName(String name) {
        return userStatusRepo.findByName(name);
    }

    @Override
    public UserStatus saveStatusWithName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("The given status name must not be null!");
        }
        UserStatus status = new UserStatus();
        status.setName(name);

        return userStatusRepo.save(status);
    }
}
