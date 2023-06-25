package ua.lpnu.notes.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.lpnu.notes.controller.authentication.KeyPair;
import ua.lpnu.notes.controller.authentication.UserInformation;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.exception.BadPasswordException;
import ua.lpnu.notes.exception.DuplicateEntityException;
import ua.lpnu.notes.repository.UserRepository;
import ua.lpnu.notes.security.JwtManager;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtManager jwtManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUser(email);
    }

    @Transactional
    public void logout(User userPrincipal) {
        if (userPrincipal != null) {
            User user = getUser(userPrincipal.getEmail());
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }

    @Transactional
    public KeyPair login(String email, String password) {
        User user = getUser(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadPasswordException("Wrong password");
        }
        String refreshToken = jwtManager.createRefreshToken(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new KeyPair(jwtManager.createAccessToken(email), refreshToken);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email %s is not found".formatted(email)));
    }

    @Transactional
    public KeyPair register(UserInformation userInformation) {
        String email = userInformation.email();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEntityException("User with %s email already exists".formatted(email));
        }

        String refreshToken = jwtManager.createRefreshToken(email);
        User user = User.builder()
                .firstName(userInformation.firstName())
                .lastName(userInformation.lastName())
                .email(email)
                .refreshToken(refreshToken)
                .password(passwordEncoder.encode(userInformation.password()))
                .build();
        userRepository.save(user);
        return new KeyPair(jwtManager.createAccessToken(email), refreshToken);
    }

    @Transactional
    public KeyPair accessToken(String refreshToken) {
        String email = jwtManager.getUserEmailByRefreshToken(refreshToken);
        if (!userRepository.existsByRefreshTokenAndEmail(refreshToken, email)) {
            throw new EntityNotFoundException("User with such refresh token is not found");
        }

        return new KeyPair(jwtManager.createAccessToken(email), refreshToken);
    }
}
