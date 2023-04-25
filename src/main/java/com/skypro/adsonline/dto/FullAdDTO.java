package com.skypro.adsonline.dto;

import lombok.Data;

@Data
public class FullAdDTO {
    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;
}
