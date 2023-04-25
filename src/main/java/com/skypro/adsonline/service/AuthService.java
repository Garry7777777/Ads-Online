package com.skypro.adsonline.service;

import com.skypro.adsonline.dto.requests.RegisterReq;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterReq registerReq);
}
