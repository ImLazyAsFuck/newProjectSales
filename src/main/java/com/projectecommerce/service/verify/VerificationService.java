package com.projectecommerce.service.verify;

import com.projectecommerce.model.entity.User;
import com.projectecommerce.model.entity.VerificationToken;
import com.projectecommerce.repository.UserRepository;
import com.projectecommerce.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    @Transactional
    public void verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ"));

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Token đã hết hạn");
        }

        User user = verificationToken.getUser();
        if (user.isVerify()) {
            throw new IllegalStateException("Tài khoản đã được xác thực");
        }

        user.setVerify(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }
}
