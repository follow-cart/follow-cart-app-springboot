package com.capstone_design.followcart.repository;

import com.capstone_design.followcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserid(String userid); // 사용자 ID로 사용자 찾기
}
