package com.projectecommerce.service.adminuser;

import com.projectecommerce.model.dto.request.AdminUpdateUserDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.PaginationDTO;
import com.projectecommerce.model.dto.response.UserSummaryDTO;
import com.projectecommerce.model.entity.User;
import com.projectecommerce.model.enums.ERole;
import com.projectecommerce.repository.UserRepository;
import com.projectecommerce.service.adminuser.AdminUserService;
import com.projectecommerce.utils.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService{
    private final UserRepository userRepository;

    @Override
    public APIResponse<PagedResultDTO<UserSummaryDTO>> listUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserSummaryDTO> userDTOs = userPage.getContent().stream()
                .map(user -> {
                    UserSummaryDTO dto = new UserSummaryDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setFullName(user.getFullName());
                    dto.setEmail(user.getEmail());
                    dto.setPhone(user.getPhone());
                    dto.setAddress(user.getAddress());
                    dto.setStatus(user.isStatus());
                    dto.setVerify(user.isVerify());
                    return dto;
                })
                .toList();

        PaginationDTO pagination = PaginationDTO.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .build();

        PagedResultDTO<UserSummaryDTO> result = PagedResultDTO.<UserSummaryDTO>builder()
                .items(userDTOs)
                .pagination(pagination)
                .build();

        return APIResponse.<PagedResultDTO<UserSummaryDTO>>builder()
                .success(true)
                .message("Danh sách người dùng")
                .data(result)
                .build();
    }


    @Override
    public APIResponse<UserSummaryDTO> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        UserSummaryDTO dto = UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .status(user.isStatus())
                .isVerify(user.isVerify())
                .build();

        return APIResponse.<UserSummaryDTO>builder()
                .success(true)
                .message("Thông tin người dùng")
                .data(dto)
                .build();
    }


    @Override
    public APIResponse<UserSummaryDTO> updateUser(Long id, AdminUpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new ConflictException("Email is already in use by another user");
        }

        if (userRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
            throw new ConflictException("Phone is already in use by another user");
        }

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setUpdateAt(LocalDate.now());

        userRepository.save(user);

        UserSummaryDTO responseDTO = UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .status(user.isStatus())
                .isVerify(user.isVerify())
                .build();

        return APIResponse.<UserSummaryDTO>builder()
                .success(true)
                .message("Cập nhật người dùng thành công")
                .data(responseDTO)
                .build();
    }

    @Override
    public APIResponse<?> updateStatus(Long id, boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if(user.isDeleted()){
            throw new ConflictException("User is already deleted");
        }
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ADMIN));

        if (isAdmin) {
            throw new ConflictException("Không thể cập nhật trạng thái của tài khoản Admin");
        }

        user.setStatus(status);
        userRepository.save(user);
        return new APIResponse<>(null, "Cập nhật trạng thái thành công", true, null, LocalDateTime.now());
    }


    @Override
    public APIResponse<?> softDelete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setDeleted(true);
        userRepository.save(user);
        return new APIResponse<>(null, "Xóa mềm người dùng thành công", true, null, LocalDateTime.now());
    }

}