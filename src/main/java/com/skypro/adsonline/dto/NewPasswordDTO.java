package com.skypro.adsonline.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Обновление пароля")
public class NewPasswordDTO {
    @Schema(description = "Текущий пароль")
    private String currentPassword;
    @Schema(description = "Новый пароль")
    private String newPassword;
}
