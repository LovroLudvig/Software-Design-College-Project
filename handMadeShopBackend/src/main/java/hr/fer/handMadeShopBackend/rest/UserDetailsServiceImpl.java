package hr.fer.handMadeShopBackend.rest;

import hr.fer.handMadeShopBackend.service.UserService;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        hr.fer.handMadeShopBackend.domain.User u = service.fetch(username);
        List<GrantedAuthority> grantedAuthorities = commaSeparatedStringToAuthorityList("ADMIN, USER");
        return new User(username, u.getPasswordHash(), grantedAuthorities);
    }

}
