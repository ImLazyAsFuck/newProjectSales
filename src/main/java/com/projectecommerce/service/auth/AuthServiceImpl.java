package com.projectecommerce.service.auth;

import com.projectecommerce.model.entity.VerificationToken;
import com.projectecommerce.repository.VerificationTokenRepository;
import com.projectecommerce.security.jwt.JWTProvider;
import com.projectecommerce.mapper.UserMapper;
import com.projectecommerce.model.dto.request.*;
import com.projectecommerce.model.dto.response.*;
import com.projectecommerce.model.entity.Role;
import com.projectecommerce.model.entity.User;
import com.projectecommerce.model.enums.ERole;
import com.projectecommerce.repository.RoleRepository;
import com.projectecommerce.repository.UserRepository;
import com.projectecommerce.service.cloudinary.CloudinaryService;
import com.projectecommerce.service.email.EmailService;
import com.projectecommerce.utils.exception.ConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CloudinaryService cloudinaryService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    @Override
    public void register(RegisterDTO dto) {
        Role role = roleRepository.findByName(ERole.CUSTOMER)
                .orElseThrow(() -> new ConflictException("Không tìm thấy role"));

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .roles(Collections.singleton(role))
                .address(dto.getAddress())
                .isVerify(false)
                .status(true)
                .createAt(LocalDate.now())
                .build();
        userRepository.save(user);

        String otp = generateOTP();
        VerificationToken token = VerificationToken.builder()
                .token(otp)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        verificationTokenRepository.save(token);

        emailService.sendVerificationEmail(user, otp);
    }


    private String generateOTP() {
        Random random = new Random();
        int otp = 100_000 + random.nextInt(900_000);
        return String.valueOf(otp);
    }

    @Override
    public APIResponse<JWTResponse> login(LoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ConflictException("Không tìm thấy người dùng"));
        if(!user.isVerify()){
            throw new ConflictException("Tài khoản chưa được xác thực");
        }

        if(user.isDeleted()){
            throw new IllegalArgumentException("Người dùng đã bị xoá");
        }
        if(!user.isStatus()){
            throw new ConflictException("Người dùng đã bị khoá");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        ERole role = user.getRoles().iterator().next().getName();

        user.setLogin(true);
        userRepository.save(user);

        JWTResponse jwtResponse = JWTResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .address(user.getAddress())
                .phone(user.getPhone())
                .role(role)
                .build();

        return APIResponse.<JWTResponse>builder()
                .data(jwtResponse)
                .success(true)
                .message("Đăng nhập thành công")
                .timeStamp(LocalDateTime.now())
                .build();
    }


    @Override
    public void changePassword(User user, ChangePasswordDTO dto) {
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ConflictException("Không tìm thấy người dùng"));
        if (!passwordEncoder.matches(dto.getOldPassword(), freshUser.getPassword())) {
            throw new ConflictException("Mật khẩu cũ không đúng");
        }
        if(!dto.getOldPassword().equals(dto.getConfirmPassword())){
            throw new ConflictException("Xác nhận mật khẩu không giống với mật khẩu");
        }

        freshUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        freshUser.setUpdateAt(LocalDate.now());
        userRepository.save(freshUser);
    }


    @Override
    public void verify(LoginDTO loginDTO, String token) {
        User user = userRepository
                .findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new ConflictException("Tài khoản và mật khẩu không khớp"));
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user)
                .orElseThrow(() -> new ConflictException("Không tìm thấy token xác thực"));

        if (!verificationToken.getToken().equals(token)) {
            throw new ConflictException("Token không hợp lệ");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token đã hết hạn");
        }

        if (user.isVerify()) throw new ConflictException("Tài khoản đã xác thực rồi");

        user.setVerify(true);
        userRepository.save(user);
        verificationTokenRepository.deleteByUser(user);
    }


    @Override
    public UserSummaryDTO getProfile(User user) {
        return UserMapper.toUserSummaryDTO(user);
    }

    @Override
    public void updateProfile(User user, UpdateProfileDTO dto) {
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId()))
            throw new ConflictException("Email đã được sử dụng");

        if (userRepository.existsByPhoneAndIdNot(dto.getPhone(), user.getId()))
            throw new ConflictException("SĐT đã được sử dụng");

        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadFile(dto.getAvatar());
                user.setAvatar(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Tải ảnh lên thất bại", e);
            }
        }

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setUpdateAt(LocalDate.now());
        userRepository.save(user);
    }

    public void validateLoginRequest(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}