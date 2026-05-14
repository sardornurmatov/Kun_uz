package uz.topnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.topnews.dto.request.*;
import uz.topnews.dto.response.*;
import uz.topnews.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/registration
     * Faqat USER roli uchun. Email orqali ro'yhatdan o'tish.
     */
    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * POST /api/v1/auth/login
     * Email va parol orqali tizimga kirish. JWT token qaytaradi.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * POST /api/v1/auth/verification
     * Email ga yuborilgan kodni tasdiqlash.
     */
    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> verify(
            @Valid @RequestBody VerificationRequest request) {
        return ResponseEntity.ok(authService.verify(request));
    }

    /**
     * POST /api/v1/auth/resend?email=xxx@mail.com
     * Tasdiqlash kodini qayta yuborish.
     */
    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<String>> resend(
            @RequestParam String email) {
        return ResponseEntity.ok(authService.resendVerification(email));
    }
}
