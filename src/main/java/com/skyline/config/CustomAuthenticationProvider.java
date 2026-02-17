package com.skyline.config;

import com.skyline.entity.Users;
import com.skyline.exception.NotFoundException;
import com.skyline.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    // This method is called automatically during login
    @Override
    public Authentication authenticate(Authentication authentication) {

        // Extract username & password from request
        String username = authentication.getName();
        String password = Objects.requireNonNull(authentication.getCredentials()).toString();

        // User Validation
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Password Validation
        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new NotFoundException("Invalid password");
        }

        // Convert roles to authorities : Spring Security understands only authorities, not roles directly
        List<SimpleGrantedAuthority> authorities =
                users.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList();

        // User is authenticated, Password is not stored,  Roles are attached
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    // Tells Spring: I support username + password authentication
    @Override
    public boolean supports(Class<?> auth) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth);
    }
}
