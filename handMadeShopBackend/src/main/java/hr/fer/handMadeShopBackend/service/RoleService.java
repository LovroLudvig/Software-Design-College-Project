package hr.fer.handMadeShopBackend.service;

import hr.fer.handMadeShopBackend.domain.Role;

public interface RoleService {

    Role findRoleByName(String name);
    Role saveRoleWithName(String name);
}
