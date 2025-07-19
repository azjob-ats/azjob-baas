package com.app.boot_app.domain.auth.security;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.repository.UserRepository;

import java.util.ArrayList;

@Service
public class FirebaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public FirebaseUserDetailsService(UserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        // In a real application, you would map the Firebase UID to your internal user ID
        // and load the user details from your database.
        // For simplicity, we'll assume the UID is the email for now.
        User user = userRepository.findByEmail(uid)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("security.user.not.found.email", new Object[]{uid}, LocaleContextHolder.getLocale())));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // Password is not used for token-based authentication
                new ArrayList<>() // Roles/Authorities would be loaded here
        );
    }
}
