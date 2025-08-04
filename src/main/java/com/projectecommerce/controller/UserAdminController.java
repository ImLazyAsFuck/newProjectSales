package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.AdminUpdateUserDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.UserSummaryDTO;
import com.projectecommerce.service.adminuser.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<APIResponse<PagedResultDTO<UserSummaryDTO>>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        APIResponse<PagedResultDTO<UserSummaryDTO>> response = adminUserService.listUsers(page - 1, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserSummaryDTO>> getUser(@PathVariable Long id) {
        APIResponse<UserSummaryDTO> response = adminUserService.getUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserSummaryDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid AdminUpdateUserDTO dto
    ) {
        APIResponse<UserSummaryDTO> response = adminUserService.updateUser(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<?>> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean status
    ) {
        APIResponse<?> response = adminUserService.updateStatus(id, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<?>> softDeleteUser(@PathVariable Long id) {
        APIResponse<?> response = adminUserService.softDelete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
