package com.skypro.adsonline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.adsonline.dto.CreateAdDTO;
import com.skypro.adsonline.enums.Role;
import com.skypro.adsonline.model.Ad;
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
import org.springframework.transaction.annotation.Transactional;
import com.skypro.adsonline.repository.AdRepository;
import com.skypro.adsonline.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class AdControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;
    private final MockPart image = new MockPart("image", "image", "image".getBytes());
    private final Ad ad = new Ad();
    private final CreateAdDTO createAdDTO = new CreateAdDTO();
    private final User user = new User();
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
        ad.setImage("image".getBytes());
        createAdDTO.setTitle("titleTest");
        createAdDTO.setDescription("descriptionTest");
        createAdDTO.setPrice(15);
        adRepository.save(ad);
    }

    @AfterEach
    void tearDown() {
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(user1)
    void testAddAd() throws Exception {

        mockMvc.perform(multipart("/ads")
                        .part(image).part(new MockPart("properties",
                                objectMapper.writeValueAsBytes(createAdDTO))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAdDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(createAdDTO.getPrice()));
    }

    @Test
    @WithMockUser(user1)
    void testGetAds() throws Exception {
        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ad.getTitle()))
                .andExpect(jsonPath("$.price").value(ad.getPrice()))
                .andExpect(jsonPath("$.description").value(ad.getDescription()));
    }

    @Test
    @WithMockUser(user1)
    void testRemoveAd() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(user2)
    void testRemoveAdOther() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = user2, roles = "ADMIN")
    void testRemoveAdByAdmin() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchTitle() throws Exception {
        mockMvc.perform(get("/ads/search?title=testTitle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }

    @Test
    @WithMockUser(user1)
    void testUpdateAd() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getPk())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject()
                        .put("price", "20")
                        .put("title", "titleTest2")
                        .put("description", "testDesc").toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ad.getTitle()));

    }

    @Test
    @WithMockUser(user2)
    void testUpdateAdAnother() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("price", "20")
                                .put("title", "titleTest2")
                                .put("description", "testDesc").toString()))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = user2, roles = "ADMIN")
    void testUpdateAdByAdmin() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("price", "20")
                                .put("title", "titleTest2")
                                .put("description", "testDesc").toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ad.getTitle()));

    }

    @Test
    @WithMockUser(user1)
    void testGetAdsForMe() throws Exception {
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(user1)
    void testUpdateImage() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getPk() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {request.addPart(image);return request;}))
                .andExpect(status().isOk());
    }

    @Test
    void testImage() throws Exception {
        mockMvc.perform(get("/ads/image/" + ad.getPk()))
                .andExpect(status().isOk())
                .andExpect(content().bytes("image".getBytes()));
    }
}