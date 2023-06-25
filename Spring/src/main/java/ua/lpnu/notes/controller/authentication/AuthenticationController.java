package ua.lpnu.notes.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.notes.dto.UserDto;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.service.AuthenticationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("me")
    public UserDto me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return mapper(user);
    }

    @GetMapping("logout")
    public void logout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authenticationService.logout(user);
    }

    @PostMapping("login")
    public KeyPair login(@RequestBody Credentials credentials) {
        return authenticationService.login(credentials.email(), credentials.password());
    }

    @PostMapping("access-token")
    public KeyPair accessToken(@RequestBody KeyPair refreshToken) {
        return authenticationService.accessToken(refreshToken.refreshToken());
    }

    @PostMapping("register")
    public KeyPair register(@RequestBody UserInformation userInformation) {
        return authenticationService.register(userInformation);
    }

    private UserDto mapper(User user) {
        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
