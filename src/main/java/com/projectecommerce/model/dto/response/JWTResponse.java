package com.projectecommerce.model.dto.response;

import com.projectecommerce.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse{
    private String token;
    private String refreshToken;

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatar;
    private String address;
    private String phone;
    private ERole role;
}
