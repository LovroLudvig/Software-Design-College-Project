package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.dao.RoleRepository;
import hr.fer.handMadeShopBackend.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public Role saveRoleWithName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("The role name must be provided!");
        }
        Role role = new Role();
        role.setName(name);

        return roleRepo.save(role);
    }
}
