package com.skypro.adsonline.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.skypro.adsonline.dto.FullUserDTO;
import com.skypro.adsonline.dto.requests.RegisterReq;
import com.skypro.adsonline.exception.NotFoundException;
import com.skypro.adsonline.service.AuthService;
import com.skypro.adsonline.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService manager;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName))  throw new NotFoundException();
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterReq registerReq) {
        if (manager.userExists(registerReq.getUsername())) return false;
        manager.createUser(FullUserDTO.toUserDetails(RegisterReq.fromRegReq(registerReq)));
        return true;
    }
}
