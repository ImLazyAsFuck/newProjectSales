package com.projectecommerce.mapper;

import com.projectecommerce.model.dto.response.UserSummaryDTO;
import com.projectecommerce.model.entity.User;

public class UserMapper {

    public static UserSummaryDTO toUserSummaryDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .status(user.isStatus())
                .isVerify(user.isVerify())
                .build();
    }


}
