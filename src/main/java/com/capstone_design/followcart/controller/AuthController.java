package com.capstone_design.followcart.controller;

import com.capstone_design.followcart.model.User;
import com.capstone_design.followcart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserid(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("로그인 성공!");
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 예외 출력
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패!");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userService.existsByUserid(user.getUserid())) {
            return ResponseEntity.badRequest().body("현재 사용 중인 ID 입니다!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return ResponseEntity.ok("회원가입 성공!");
    }
    @GetMapping("/check-userid")
    public ResponseEntity<?> checkUserid(@RequestParam String userid) {
        if (userService.existsByUserid(userid)) {
            return ResponseEntity.badRequest().body("현재 사용 중인 ID 입니다!");
        }
        return ResponseEntity.ok("사용 가능한 ID 입니다!");
    }
}
