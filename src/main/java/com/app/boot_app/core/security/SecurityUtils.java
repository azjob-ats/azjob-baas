package com.app.boot_app.core.security;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.boot_app.domain.auth.entity.User;
import com.app.boot_app.domain.auth.repository.UserRepository;
import com.app.boot_app.shared.exeception.model.NotFoundException;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public SecurityUtils(UserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new NotFoundException(
                        "security/getAuthenticatedUser", 
                        "security authenticated user not found"));
        } else {
            throw new NotFoundException("security/getAuthenticatedUser", 
            "An error occurred while retrieving the authenticated user.");
        }
    }
}
