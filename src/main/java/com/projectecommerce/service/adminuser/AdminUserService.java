package com.projectecommerce.service.adminuser;

import com.projectecommerce.model.dto.request.AdminUpdateUserDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.UserSummaryDTO;

public interface AdminUserService {
    APIResponse<PagedResultDTO<UserSummaryDTO>> listUsers(int page, int size);
    APIResponse<UserSummaryDTO> getUserById(Long id);
    APIResponse<UserSummaryDTO> updateUser(Long id, AdminUpdateUserDTO dto);
    APIResponse<?> updateStatus(Long id, boolean status);
    APIResponse<?> softDelete(Long id);
}
