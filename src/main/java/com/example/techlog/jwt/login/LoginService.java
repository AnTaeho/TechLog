package com.example.techlog.jwt.login;

import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password("password")
                .roles(user.getRole().name())
                .build();
    }
}
