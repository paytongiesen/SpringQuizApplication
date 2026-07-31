package com.example.demo.service;

import com.example.demo.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    
    private Map<String, User> users = new HashMap<>();

    public QuizUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public void registerUser(String username, String email, String password, String role) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword, role);
        users.put(username, user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username does not exist");
        }
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}