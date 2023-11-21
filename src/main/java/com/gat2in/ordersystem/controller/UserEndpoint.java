package com.gat2in.ordersystem.controller;


import com.gat2in.ordersystem.model.Role;
import com.gat2in.ordersystem.model.UserEntity;
import com.gat2in.ordersystem.payload.*;
import com.gat2in.ordersystem.repository.UserRepository;
import com.gat2in.ordersystem.service.JwtTokenProvider;
import com.gat2in.ordersystem.service.RoleService;
import com.gat2in.ordersystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Optional<UserEntity> optUser = userService.findByUsername(loginRequest.getUsername());

        UserEntity user = null;
        

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        if(user == null)
           return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, ""));
        else
           return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getUsername()));
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {

        String role = payload.getRole();

        Role roleExist = roleService.findByName(role);
        if(roleExist == null) {
            return ResponseEntity.ok().body(new ApiResponse(false,
                    "Unable to create user: role does not exist")
            );
        }

        UserEntity user = UserEntity
                .builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .role(payload.getRole())
                .build();

        try {
            UserEntity savedUser = userService.registerUser(user);
            log.info("saved user: {}",savedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(new ApiResponse(true,
                "User created successfully, to register click on your activation link which we have sent on your email")
        );
    }

    
}