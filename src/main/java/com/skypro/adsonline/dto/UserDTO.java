package com.skypro.adsonline.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Схема пользователя")
public class UserDTO {
    @Schema(description = "id пользователя")
    private Integer id;
    @Schema(description = "Логин пользователя")
    private String email;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамлия пользователя")
    private String lastName;
    @Schema(description = "Телефон пользователя")
    private String phone;
    @Schema(description = "Ссылка на аватар пользователя")
    private String image;
}
