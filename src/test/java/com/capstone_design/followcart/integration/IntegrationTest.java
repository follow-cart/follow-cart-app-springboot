package com.capstone_design.followcart.integration;

import com.capstone_design.followcart.model.User;
import com.capstone_design.followcart.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MvcResult mvcResult;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUserid("testuser");
        user.setUsername("testusername");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        if (mvcResult != null) {
            logger.info("HTTP Status: {}", mvcResult.getResponse().getStatus());
            try {
                logger.info("Response: {}", mvcResult.getResponse().getContentAsString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void 회원가입_테스트() throws Exception {
        User newUser = new User();
        newUser.setUserid("newuser");
        newUser.setUsername("newusername");
        newUser.setPassword("123456");

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("회원가입 성공!")));

        mvcResult = resultActions.andReturn();
    }

    @Test
    public void 중복_회원가입_테스트() throws Exception {
        User newUser = new User();
        newUser.setUserid("testuser");
        newUser.setUsername("testusername");
        newUser.setPassword("123456");

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("현재 사용 중인 ID 입니다!")));

        mvcResult = resultActions.andReturn();
    }

    @Test
    public void 로그인_테스트() throws Exception {
        User loginRequest = new User();
        loginRequest.setUserid("testuser");
        loginRequest.setPassword("password");

        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("로그인 성공!")));

        mvcResult = resultActions.andReturn();
    }

    @Test
    public void 잘못된_로그인_테스트() throws Exception {
        User loginRequest = new User();
        loginRequest.setUserid("testuser");
        loginRequest.setPassword("wrongpassword");

        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$", is("로그인 실패!")));

        mvcResult = resultActions.andReturn();
    }
}
