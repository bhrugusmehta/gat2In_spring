package com.gat2in.ordersystem.service;


import com.gat2in.ordersystem.model.UserEntity;
import com.gat2in.ordersystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;


@Service
@Slf4j
public class UserService {
    @Autowired

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public UserEntity registerUser(UserEntity user) {

        if(userRepository.existsByUsername(user.getUsername())) {
            return null;
        }



        user.setRole(user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        if (savedUser != null)
            userRepository.save(user);
        return savedUser;
    }
    

}
