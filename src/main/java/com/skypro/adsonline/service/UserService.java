package com.skypro.adsonline.service;

import com.skypro.adsonline.dto.FullUserDTO;
import com.skypro.adsonline.dto.NewPasswordDTO;
import com.skypro.adsonline.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.skypro.adsonline.exception.NotFoundException;
import com.skypro.adsonline.model.User;
import com.skypro.adsonline.repository.UserRepository;

import java.io.IOException;

@Service
public class UserService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;


    public UserDTO findUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new).toDTO();
    }

    public void changePassword(NewPasswordDTO newPasswordDTO, Authentication authentication) {
        var user = userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new);
        var checkPass = encoder.matches(newPasswordDTO.getCurrentPassword(),user.getPassword());
        if ( ! checkPass ) throw new BadCredentialsException("Authentication exception");
        user.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(NotFoundException::new);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
        return userDTO;
    }

    public boolean updateAvatar(Authentication authentication, MultipartFile avatar) throws IOException {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new);
        user.setImage(avatar.getBytes());
        userRepository.save(user);
        return true;
    }

    public byte[] getAvatar(Integer id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new).getImage();
    }


    @Override
    public void createUser(UserDetails userDetails) {
        User user = ((FullUserDTO) userDetails).toUser();
        user.setPassword(encoder.encode(userDetails.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return FullUserDTO.toUserDetails(userRepository.findByUsername(username).orElseThrow(NotFoundException::new));
    }
}
