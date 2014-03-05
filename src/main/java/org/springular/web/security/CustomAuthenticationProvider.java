package org.springular.web.security;

import org.springular.model.Role;
import org.springular.model.User;
import org.springular.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Authenticates a user.
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Inject
    UserRepository repo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = repo.findByUserIdIgnoreCase(userId);
        if (user != null && user.getActive() && authenticateUser(userId, password)){
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            for(Role role : user.getRoles()){
                grantedAuths.add(new SimpleGrantedAuthority(role.getName()));
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(userId, password, grantedAuths);
            return auth;
        }
        else {
            return null;
        }
    }

    private boolean authenticateUser(String userId, String password) {
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
