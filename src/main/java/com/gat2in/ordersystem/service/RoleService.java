package com.gat2in.ordersystem.service;

import com.gat2in.ordersystem.model.Role;
import com.gat2in.ordersystem.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
