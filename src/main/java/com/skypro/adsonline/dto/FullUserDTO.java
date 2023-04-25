package com.skypro.adsonline.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skypro.adsonline.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.skypro.adsonline.enums.Role;

import java.util.Collection;
import java.util.List;

@Data
public class FullUserDTO implements UserDetails {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String image;
    private boolean enabled;
    private Role role;


    public static FullUserDTO toUserDetails(User user) {
        FullUserDTO fullUserDTO = new FullUserDTO();
        BeanUtils.copyProperties(user, fullUserDTO);
        fullUserDTO.setImage("/users/me/image/" + user.getId());
        return fullUserDTO;
    }

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
