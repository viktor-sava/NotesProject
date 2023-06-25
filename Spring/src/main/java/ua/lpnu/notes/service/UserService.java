package ua.lpnu.notes.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email %s is not found".formatted(email)));
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

}
