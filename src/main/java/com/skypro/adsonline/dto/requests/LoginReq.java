package com.skypro.adsonline.dto.requests;

import lombok.Data;

@Data
public class LoginReq {
    private String password;
    private String username;

}
