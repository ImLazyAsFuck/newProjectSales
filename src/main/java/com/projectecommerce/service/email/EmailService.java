package com.projectecommerce.service.email;

import com.projectecommerce.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String senderEmail;

    private String verificationUrl = "http://localhost:8080/";

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String subject = "Xác thực tài khoản";
        String verificationLink = verificationUrl + "?token=" + token;

        String body = """
                Xin chào %s,

                Vui lòng xác thực tài khoản của bạn bằng cách nhấn vào liên kết sau:
                %s

                Link có hiệu lực trong 24 giờ.
                """.formatted(user.getFullName(), verificationLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
