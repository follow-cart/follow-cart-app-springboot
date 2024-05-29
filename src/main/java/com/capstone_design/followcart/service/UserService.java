package com.capstone_design.followcart.service;

import com.capstone_design.followcart.model.User;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
