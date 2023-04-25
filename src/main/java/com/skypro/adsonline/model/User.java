package com.skypro.adsonline.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import com.skypro.adsonline.dto.UserDTO;
import com.skypro.adsonline.enums.Role;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private byte[] image;
    private String password;
    private Boolean enabled=Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE})
    private List<Ad> ads;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE})
    private List<Comment> comments;

    public  UserDTO toDTO() {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(this, userDTO);
        userDTO.setEmail(this.getUsername());
        userDTO.setImage("/users/me/image/" + this.getId());
        return userDTO;
    }
}
