package com.projectecommerce.service.auth;

import com.projectecommerce.model.dto.request.ChangePasswordDTO;
import com.projectecommerce.model.dto.request.LoginDTO;
import com.projectecommerce.model.dto.request.RegisterDTO;
import com.projectecommerce.model.dto.request.UpdateProfileDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.JWTResponse;
import com.projectecommerce.model.dto.response.UserSummaryDTO;
import com.projectecommerce.model.entity.User;

public interface AuthService {
    void register(RegisterDTO registerDTO);
    APIResponse<JWTResponse> login(LoginDTO loginDTO);
    void verify(LoginDTO loginDTO, String token);
    UserSummaryDTO getProfile(User user);
    void updateProfile(User user, UpdateProfileDTO dto);
    void changePassword(User user, ChangePasswordDTO dto);
}
