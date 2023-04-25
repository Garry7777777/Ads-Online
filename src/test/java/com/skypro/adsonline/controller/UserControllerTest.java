package com.skypro.adsonline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.adsonline.dto.UserDTO;
import com.skypro.adsonline.enums.Role;
import com.skypro.adsonline.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.skypro.adsonline.exception.NotFoundException;
import com.skypro.adsonline.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    private final User user = new User();
    private final MockPart mockPart = new MockPart("image", "avatar", "avatar1".getBytes());
    private final String url = "/users/me/image/";
    private final String user1 = "user1@gmail.com";
    private final String user2 = "user2@gmail.com";

    @BeforeEach
    void setUp() {
        user.setUsername(user1);
        user.setFirstName("User1");
        user.setLastName("User1");
        user.setPhone("+71111111111");
        user.setPassword(encoder.encode("user1user"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        user.setImage("avatar1".getBytes());
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(user1)
    void testSetPassword() throws Exception {

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("currentPassword", "user1user")
                                .put("newPassword", "user11user").toString()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(user1)
    void testGetUser() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getUsername()));
    }

    @Test
    @WithMockUser(user1)
    void testUpdateUser() throws Exception {
        UserDTO userDTO = userRepository.findByUsername(user.getUsername()).orElseThrow(NotFoundException::new).toDTO();
        userDTO.setEmail("user2@gmail.com");
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value("user2@gmail.com"));
    }

    @Test
    @WithMockUser(user1)
    void testUpdateImage() throws Exception {

        mockMvc.perform(patch(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {request.addPart(mockPart);return request;}))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(user1)
    void testGetImage() throws Exception {
        User testUser = userRepository.findByUsername(user.getUsername()).orElseThrow(NotFoundException::new);
        mockMvc.perform(get(url + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().bytes("avatar1".getBytes()));
   }
}