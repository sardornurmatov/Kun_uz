package uz.topnews.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import uz.topnews.entity.ProfileEntity;
import uz.topnews.repository.ProfileRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByUsernameAndVisibleTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Foydalanuvchi topilmadi: " + username));

        var authorities = profile.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRole().name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                profile.getUsername(),
                profile.getPassword(),
                authorities
        );
    }
}
