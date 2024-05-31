package com.capstone_design.followcart.unit;

import com.capstone_design.followcart.model.User;
import com.capstone_design.followcart.repository.UserRepository;
import com.capstone_design.followcart.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUserid("testuser");
        testUser.setUsername("testusername");
        testUser.setPassword("password");
    }

    @AfterEach
    public void tearDown() {
        logger.info("테스트 완료: {}", testUser);
    }

    @Test
    public void 사용자_저장_테스트() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.saveUser(testUser);

        assertEquals("testuser", savedUser.getUserid());
        verify(userRepository, times(1)).save(testUser);
        logger.info("사용자 저장 테스트 성공");
    }

    @Test
    public void 사용자명으로_찾기_테스트() {
        when(userRepository.findByUsername("testusername")).thenReturn(testUser);

        User foundUser = userService.findByUsername("testusername");

        assertEquals("testusername", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testusername");
        logger.info("사용자명으로 찾기 테스트 성공");
    }

    @Test
    public void 사용자명_존재_여부_테스트() {
        when(userRepository.findByUsername("testusername")).thenReturn(testUser);

        boolean exists = userService.existsByUsername("testusername");

        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername("testusername");
        logger.info("사용자명 존재 여부 테스트 성공");
    }

    @Test
    public void 사용자ID_존재_여부_테스트() {
        when(userRepository.findByUserid("testuser")).thenReturn(testUser);

        boolean exists = userService.existsByUserid("testuser");

        assertTrue(exists);
        verify(userRepository, times(1)).findByUserid("testuser");
        logger.info("사용자ID 존재 여부 테스트 성공");
    }

    @Test
    public void 사용자ID로_사용자_로드_테스트() {
        when(userRepository.findByUserid("testuser")).thenReturn(testUser);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).findByUserid("testuser");
        logger.info("사용자ID로 사용자 로드 테스트 성공");
    }

    @Test
    public void 사용자ID로_사용자_로드_실패_테스트() {
        when(userRepository.findByUserid("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });

        verify(userRepository, times(1)).findByUserid("nonexistent");
        logger.info("사용자ID로 사용자 로드 실패 테스트 성공");
    }
}
