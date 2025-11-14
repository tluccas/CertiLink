package com.alvesdev.CertiLink.service.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alvesdev.CertiLink.config.security.UserAuthenticated;
import com.alvesdev.CertiLink.model.entity.Users.User;
import com.alvesdev.CertiLink.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

    private final UserRepository userRepository;

    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()){
            userOpt = userRepository.findByEmail(username);
        }

        User user = userOpt.orElseThrow(
            () -> new UsernameNotFoundException("User not found")
        );

        return new UserAuthenticated(user);
    }
    
}
