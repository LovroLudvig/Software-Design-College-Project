package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.Exceptions.NotFoundException;
import hr.fer.handMadeShopBackend.dao.RoleRepository;
import hr.fer.handMadeShopBackend.dao.TownRepository;
import hr.fer.handMadeShopBackend.dao.UserRepository;
import hr.fer.handMadeShopBackend.dao.UserStatusRepository;
import hr.fer.handMadeShopBackend.domain.Role;
import hr.fer.handMadeShopBackend.domain.Town;
import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.domain.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private TownRepository townRepository;

    @Override
    public User fetch(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(User user) {
        UserStatus status = userStatusRepository.findByName(Constants.USER_STATUS_ALLOWED);
        if(status == null) {
            throw new IllegalArgumentException("Not status with name " + Constants.USER_STATUS_ALLOWED);
        }
        user.setUserStatus(status);

        Role role = roleRepo.findByName(Constants.ROLE_USER);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        Town town = checkTown(user.getTown());
        user.setTown(town);

        if(userRepo.countByUsername(user.getUsername()) != 0) {
            throw new IllegalArgumentException("The user already exists!");
        }
        return userRepo.save(user);
    }

    @Override
    public User saveAdminUser(User user) {
        UserStatus status = userStatusRepository.findByName(Constants.USER_STATUS_ALLOWED);
        if(status == null) {
            throw new IllegalArgumentException("No status with name " + Constants.USER_STATUS_ALLOWED);
        }
        user.setUserStatus(status);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepo.findByName(Constants.ROLE_USER));
        roles.add(roleRepo.findByName(Constants.ROLE_ADMIN));
        user.setRoles(roles);

        Town town = checkTown(user.getTown());
        user.setTown(town);

        if(userRepo.countByUsername(user.getUsername()) != 0) {
            throw new IllegalArgumentException("The user already exists!");
        }
        return userRepo.save(user);
    }

    @Override
    public User deleteUser(String username) {
        validateUserWithUsername(username);
        userRepo.deleteByUsername(username);
        return null;
    }

    @Override
    public User forbidAccess(String username) {
        validateUserWithUsername(username);

        User u = userRepo.findByUsername(username);
        UserStatus forbidden = userStatusRepository.findByName(Constants.USER_STATUS_FORBIDDEN);

        u.setUserStatus(forbidden);

        return userRepo.save(u);
    }

    private void validateUserWithUsername(String username) {
        if(username == null) {
            throw new IllegalArgumentException("The username of the user must be provided!");
        }
        User u = userRepo.findByUsername(username);
        if(u == null) {
            throw new NotFoundException("The specified user is not found!");
        }
    }

    @Override
    public User updateUserInfo(User user) {
        User u = validate(user);

        String hash = u.getPasswordHash();
        
        user.setPasswordHash(hash);
        user.setRoles(u.getRoles());

        UserStatus status = userStatusRepository.findByName(Constants.USER_STATUS_ALLOWED);
        user.setUserStatus(status);

        Town town = checkTown(user.getTown());
        user.setTown(town);

        return userRepo.save(user);
    }

    private User validate(User user) {
        if(user == null) {  throw new IllegalArgumentException("User object must be given"); }
        User u = userRepo.findByUsername(user.getUsername());
        if(u == null) {
            throw new NotFoundException("The specified user is not found!");
        }
        return u;
    }

    private Town checkTown(Town town) {
        if(town == null) return null;
        Optional<Town> t = townRepository.findById(town.getPostCode());
        if(!t.isPresent()) {
            return townRepository.save(town);
        }
        return t.get();
    }

}
