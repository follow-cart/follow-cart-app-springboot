package com.capstone_design.followcart.unit;

import com.capstone_design.followcart.controller.AuthController;
import com.capstone_design.followcart.model.User;
import com.capstone_design.followcart.repository.UserRepository;
import com.capstone_design.followcart.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository.deleteAll();
        User user = new User();
        user.setUserid("testuser");
        user.setUsername("testusername");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @Test
    public void 회원가입_테스트() throws Exception {
        User newUser = new User();
        newUser.setUserid("newuser");
        newUser.setUsername("newusername");
        newUser.setPassword("123456");

        when(userService.existsByUsername("newusername")).thenReturn(false);
        when(userService.existsByUserid("newuser")).thenReturn(false);
        when(userService.saveUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User registered successfully")));
    }

    @Test
    public void 중복_회원가입_테스트() throws Exception {
        User newUser = new User();
        newUser.setUserid("testuser");
        newUser.setUsername("testusername");
        newUser.setPassword("123456");

        when(userService.existsByUsername("testusername")).thenReturn(true);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Username is already taken")));
    }

    @Test
    public void 로그인_테스트() throws Exception {
        User loginRequest = new User();
        loginRequest.setUserid("testuser");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any())).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Login successful")));
    }

    @Test
    public void 잘못된_로그인_테스트() throws Exception {
        User loginRequest = new User();
        loginRequest.setUserid("testuser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
