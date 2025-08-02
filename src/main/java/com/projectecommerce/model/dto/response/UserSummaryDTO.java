package com.projectecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private boolean status;
    private boolean isVerify;
}