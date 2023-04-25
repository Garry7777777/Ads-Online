package com.skypro.adsonline.dto.requests;

import com.skypro.adsonline.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.skypro.adsonline.enums.Role;

@Data
public class RegisterReq {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    public static User fromRegReq(RegisterReq registerReq) {
        User user = new User();
        BeanUtils.copyProperties(registerReq, user );
        user.setRole(Role.USER);
        return user;
    }
}
