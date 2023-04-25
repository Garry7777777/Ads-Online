package com.skypro.adsonline.service;

import com.skypro.adsonline.dto.AdDTO;
import com.skypro.adsonline.dto.CreateAdDTO;
import com.skypro.adsonline.dto.FullAdDTO;
import com.skypro.adsonline.dto.responses.Response;
import com.skypro.adsonline.model.Ad;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.skypro.adsonline.exception.NotFoundException;
import com.skypro.adsonline.model.User;
import com.skypro.adsonline.repository.AdRepository;
import com.skypro.adsonline.repository.UserRepository;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private  UserRepository userRepository;



    public Response<AdDTO> getAll() {
        return Response.Wrapper(adRepository.findAll().stream().map(Ad::toDTO).collect(Collectors.toList()));
    }

    public AdDTO createAd(MultipartFile imageFile, CreateAdDTO createAdDTO, Authentication authentication) throws IOException {
        Ad ad = Ad.fromCreateAdDTO(createAdDTO);
        ad.setImage(imageFile.getBytes());
        ad.setAuthor(userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new));
        return adRepository.save(ad).toDTO();
    }


    public FullAdDTO getById(Integer id) {
        return adRepository.findById(id).orElseThrow(NotFoundException::new).toFullDTO();
    }

    @Transactional
    public void remove(Integer id) {
        adRepository.deleteById(id);
    }

    @Transactional
    public AdDTO update(Integer id, CreateAdDTO createAds) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(createAds, ad);
        return adRepository.save(ad).toDTO();
    }

    public Response<AdDTO> getMyAds(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new);
        return Response.Wrapper(adRepository.findAllByAuthorId(user.getId()).stream().map(Ad::toDTO).collect(Collectors.toList()));
    }

    @Transactional
    public byte[] updateImage(Integer id, MultipartFile avatar) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        try {
            byte[] bytes = avatar.getBytes();
            ad.setImage(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);}
        return adRepository.saveAndFlush(ad).getImage();
    }

    public byte[] showImage(Integer id) {
        return adRepository.findById(id).orElseThrow(NotFoundException::new).getImage();
    }

    public Response<AdDTO> searchTitle(String title) {
        return Response.Wrapper(adRepository.findAllByTitleContains(title)
                .stream().map(Ad::toDTO).collect(Collectors.toList()));
    }
}
