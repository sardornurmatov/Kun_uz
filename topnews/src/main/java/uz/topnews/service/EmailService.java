package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.topnews.entity.EmailHistoryEntity;
import uz.topnews.exception.AppException;
import uz.topnews.repository.EmailHistoryRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailHistoryRepository emailHistoryRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 1 daqiqa ichida maksimum 4 ta email
    private static final int MAX_EMAIL_PER_MINUTE = 4;

    public void sendVerificationEmail(String toEmail, String code) {
        // Limit tekshirish
        checkEmailLimit(toEmail);

        String subject = "TopNews - Email tasdiqlash";
        String message = "Sizning tasdiqlash kodingiz: " + code +
                "\nKod 10 daqiqa davomida amal qiladi.";

        sendEmail(toEmail, subject, message);
    }

    public void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            mail.setTo(toEmail);
            mail.setSubject(subject);
            mail.setText(text);
            mailSender.send(mail);

            // Tarix saqlash
            saveEmailHistory(toEmail, text);
            log.info("Email yuborildi: {}", toEmail);
        } catch (Exception e) {
            log.error("Email yuborishda xatolik: {}", e.getMessage());
            throw new AppException("Email yuborishda xatolik yuz berdi");
        }
    }

    private void checkEmailLimit(String email) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        Long count = emailHistoryRepository.countByEmailAndCreatedDateAfter(email, oneMinuteAgo);
        if (count >= MAX_EMAIL_PER_MINUTE) {
            throw new AppException("1 daqiqa ichida " + MAX_EMAIL_PER_MINUTE +
                    " tadan ko'p email yuborib bo'lmaydi. Keyinroq urinib ko'ring.");
        }
    }

    private void saveEmailHistory(String email, String message) {
        EmailHistoryEntity history = EmailHistoryEntity.builder()
                .email(email)
                .message(message)
                .build();
        emailHistoryRepository.save(history);
    }
}
