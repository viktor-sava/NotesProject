package ua.lpnu.notes.security;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ua.lpnu.notes.entity.User;
import ua.lpnu.notes.exception.AccessTokenNotValidException;
import ua.lpnu.notes.service.UserService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final HandlerExceptionResolver resolver;

    public JwtFilter(JwtManager jwtManager,
                     UserService userService,
                     AuthenticationManager authenticationManager,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtManager = jwtManager;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/auth/login") || servletPath.equals("/auth/access-token") || servletPath.equals("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authorize(request, response, filterChain);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    private void authorize(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AccessTokenNotValidException("Header Authorization is empty");
        }
        String token = authorization.substring(7);
        String email = jwtManager.getUserEmail(token);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User is not found");
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
