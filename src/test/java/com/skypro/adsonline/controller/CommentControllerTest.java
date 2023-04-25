package com.skypro.adsonline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.adsonline.dto.CommentDTO;
import com.skypro.adsonline.enums.Role;
import com.skypro.adsonline.model.Ad;
import com.skypro.adsonline.model.Comment;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.skypro.adsonline.repository.AdRepository;
import com.skypro.adsonline.repository.CommentRepository;
import com.skypro.adsonline.repository.UserRepository;
import java.time.Instant;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    private final User user = new User();
    private final Ad ad = new Ad();
    private final Comment comment = new Comment();
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
        userRepository.save(user);
        ad.setAuthor(user);
        ad.setTitle("testTitle");
        ad.setDescription("testDescription");
        ad.setPrice(10);
        adRepository.save(ad);
        comment.setCreated(Instant.now());
        comment.setText("testText");
        comment.setAd(ad);
        comment.setAuthor(user);
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(user1)
    void testGetComment() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getPk() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(user1)
    void testAddComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("testText");
        mockMvc.perform(post("/ads/" + ad.getPk() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("testText"));
    }

    @Test
    @WithMockUser(user1)
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getPk() + "/comments/" + comment.getPk()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(user2)
    void testDeleteCommentByOther() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getPk() + "/comments/" + comment.getPk()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = user2, roles = "ADMIN")
    void testDeleteCommentByAdmin() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getPk() + "/comments/" + comment.getPk()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(user1)
    void testUpdateComment() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getPk() + "/comments/" + comment.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject().put("text", "testText").toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("testText"));
    }
}