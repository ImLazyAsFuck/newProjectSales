package com.projectecommerce.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ChangePasswordDTO {
    @NotBlank private String oldPassword;
    @NotBlank private String confirmPassword;
    @NotBlank @Size(min = 6) private String newPassword;
}
