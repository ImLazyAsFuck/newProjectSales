package com.projectecommerce.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdateUserDTO {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank( message = "Email cannot be blank")
    @Email(message = "Email is invalid (ex: example@domain.sth)")
    private String email;

    @NotBlank( message = "Phone cannot be blank")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number is invalid")
    private String phone;

    @NotBlank( message = "Address cannot be blank")
    private String address;
}