package com.myweddi.config;

import com.myweddi.users.authentication.Auth;
import com.myweddi.users.authentication.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class WeddingDetailsService implements UserDetailsService {

    private AuthRepository authRepository;

    @Autowired
    public WeddingDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + "not found"));
    }
}

