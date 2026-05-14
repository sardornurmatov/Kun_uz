package uz.topnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.topnews.dto.request.*;
import uz.topnews.dto.response.*;
import uz.topnews.entity.*;
import uz.topnews.enums.*;
import uz.topnews.exception.AppException;
import uz.topnews.repository.*;
import uz.topnews.security.JwtUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // In-memory verification storage (Redis o'rniga sodda variant)
    // Production da Redis ishlatiladi
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> verificationExpiry = new HashMap<>();
    private static final long VERIFICATION_EXPIRY_MINUTES = 10;

    // ============================================================
    // 1. REGISTRATION
    // ============================================================
    @Transactional
    public ApiResponse<String> register(RegistrationRequest request) {
        // Email mavjudligini tekshirish
        if (profileRepository.existsByUsername(request.getUsername())) {
            throw new AppException("Bu email allaqachon ro'yhatdan o'tgan");
        }

        // Profile yaratish
        ProfileEntity profile = ProfileEntity.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(ProfileStatus.NOT_ACTIVE)
                .visible(true)
                .build();

        profileRepository.save(profile);

        // Default ROLE_USER berish
        ProfileRoleEntity roleEntity = ProfileRoleEntity.builder()
                .profile(profile)
                .role(ProfileRole.ROLE_USER)
                .build();
        profileRoleRepository.save(roleEntity);

        // Verification kodi yuborish
        sendVerificationCode(request.getUsername());

        return ApiResponse.ok("Ro'yhatdan o'tdingiz! Emailingizga tasdiqlash kodi yuborildi.");
    }

    // ============================================================
    // 2. LOGIN
    // ============================================================
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        // Foydalanuvchi mavjudligini tekshirish
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(request.getUsername())
                .orElseThrow(() -> new AppException("Email yoki parol noto'g'ri"));

        // Status tekshirish
        if (profile.getStatus() == ProfileStatus.BLOCKED) {
            throw new AppException("Hisobingiz bloklangan. Admin bilan bog'laning.");
        }
        if (profile.getStatus() == ProfileStatus.NOT_ACTIVE) {
            throw new AppException("Hisobingiz faol emas. Emailni tasdiqlang.");
        }

        // Parolni tekshirish
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppException("Email yoki parol noto'g'ri");
        }

        // JWT token yaratish
        String token = jwtUtil.generateToken(profile.getUsername());

        // Rollarni olish
        List<ProfileRole> roles = profile.getRoles().stream()
                .map(ProfileRoleEntity::getRole)
                .collect(Collectors.toList());

        LoginResponse response = LoginResponse.builder()
                .name(profile.getName())
                .surname(profile.getSurname())
                .username(profile.getUsername())
                .roleList(roles)
                .token(token)
                .build();

        return ApiResponse.ok(response);
    }

    // ============================================================
    // 3. VERIFICATION (Email orqali)
    // ============================================================
    public ApiResponse<String> verify(VerificationRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        // Kodni tekshirish
        String savedCode = verificationCodes.get(email);
        Long expiry = verificationExpiry.get(email);

        if (savedCode == null) {
            throw new AppException("Tasdiqlash kodi topilmadi yoki muddati o'tgan");
        }

        if (System.currentTimeMillis() > expiry) {
            verificationCodes.remove(email);
            verificationExpiry.remove(email);
            throw new AppException("Tasdiqlash kodi muddati o'tgan");
        }

        if (!savedCode.equals(code)) {
            throw new AppException("Tasdiqlash kodi noto'g'ri");
        }

        // Profilni faollashtirish
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(email)
                .orElseThrow(() -> new AppException("Foydalanuvchi topilmadi"));

        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);

        // Kodni o'chirish
        verificationCodes.remove(email);
        verificationExpiry.remove(email);

        return ApiResponse.ok("Email muvaffaqiyatli tasdiqlandi!");
    }

    // ============================================================
    // 4. RESEND VERIFICATION CODE
    // ============================================================
    public ApiResponse<String> resendVerification(String email) {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(email)
                .orElseThrow(() -> new AppException("Foydalanuvchi topilmadi"));

        if (profile.getStatus() == ProfileStatus.ACTIVE) {
            throw new AppException("Hisob allaqachon faol");
        }

        sendVerificationCode(email);
        return ApiResponse.ok("Yangi tasdiqlash kodi yuborildi");
    }

    // ============================================================
    // HELPER: Verification kodi yuborish
    // ============================================================
    private void sendVerificationCode(String email) {
        // 6 xonali random kod
        String code = String.format("%06d", new Random().nextInt(999999));

        // Saqlash (10 daqiqa)
        verificationCodes.put(email, code);
        verificationExpiry.put(email,
                System.currentTimeMillis() + VERIFICATION_EXPIRY_MINUTES * 60 * 1000);

        // Email yuborish
        emailService.sendVerificationEmail(email, code);
    }
}
