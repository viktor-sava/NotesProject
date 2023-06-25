package ua.lpnu.notes.security;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.lpnu.notes.exception.AccessTokenNotValidException;

import java.io.IOException;

@Component
public class ExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof AccessTokenNotValidException) {
                response.sendError(401);
                logger.info(e.getMessage(), e.getCause());
                return;
            }
            if (e instanceof EntityNotFoundException) {
                response.sendError(403);
                logger.info(e.getMessage(), e.getCause());
                return;
            }
            logger.info(e.getMessage(), e.getCause());
        }
    }
}
