package com.skypro.adsonline.controller;

import com.skypro.adsonline.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.skypro.adsonline.enums.Role;
import com.skypro.adsonline.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    private final String rawPassword ="user1user";
    User user = new User();
    private final String user1 = "user1@gmail.com";
    private final String user2 = "user2@gmail.com";

    @BeforeEach
    void setUp() {
        user.setUsername(user1);
        user.setFirstName("User1");
        user.setLastName("User1");
        user.setPhone("+71111111111");
        user.setPassword(encoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("username", user.getUsername())
                                .put("password", rawPassword).toString()))
                .andExpect(status().isOk());
    }
    @Test
    void testRegister() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("username", user2)
                                .put("password", rawPassword)
                                .put("firstName", user2)
                                .put("lastName", user2)
                                .put("phone", "+72222222222")
                                .put("role", Role.USER.toString())
                                .toString()))
                .andExpect(status().isOk());
    }
}