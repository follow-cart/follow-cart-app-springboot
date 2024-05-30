package com.capstone_design.followcart.unit;

import com.capstone_design.followcart.model.User;
import com.capstone_design.followcart.repository.UserRepository;
import com.capstone_design.followcart.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 사용자_저장_테스트() {
        User user = new User();
        user.setUserid("testuser");
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertEquals("testuser", savedUser.getUserid());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void 사용자명으로_찾기_테스트() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.findByUsername("testuser");

        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void 사용자명_존재_여부_테스트() {
        when(userRepository.findByUsername("testuser")).thenReturn(new User());

        boolean exists = userService.existsByUsername("testuser");

        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void 사용자ID_존재_여부_테스트() {
        when(userRepository.findByUserid("testuser")).thenReturn(new User());

        boolean exists = userService.existsByUserid("testuser");

        assertTrue(exists);
        verify(userRepository, times(1)).findByUserid("testuser");
    }

    @Test
    public void 사용자ID로_사용자_로드_테스트() {
        User user = new User();
        user.setUserid("testuser");
        user.setPassword("password");

        when(userRepository.findByUserid("testuser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).findByUserid("testuser");
    }

    @Test
    public void 사용자ID로_사용자_로드_실패_테스트() {
        when(userRepository.findByUserid("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });

        verify(userRepository, times(1)).findByUserid("nonexistent");
    }
}
