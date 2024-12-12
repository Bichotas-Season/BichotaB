package com.bichotas.moduloprestamos.config;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bichotas.moduloprestamos.service.ApiClient;

/**
 * JwtAuthenticationFilter is a filter that processes incoming HTTP requests to check for a valid JWT token.
 * If a valid token is found, it sets the authentication context with the user's role.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLE_CLAIM = "\"role\":";

    private final ApiClient apiClient;

    public JwtAuthenticationFilter(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Filters incoming HTTP requests to check for a valid JWT token.
     * If a valid token is found, it sets the authentication context with the user's role.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during the filtering process
     * @throws IOException      if an I/O error occurs during the filtering process
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && apiClient.validateToken(jwt)) {
                String role = extractRoleFromJwt(jwt);
                setAuthenticationContext(request, role);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to process JWT authentication", ex);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the given HTTP request.
     *
     * @param request the HttpServletRequest object containing the client's request
     * @return the JWT token as a String if present and valid, otherwise null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * Extracts the role from the JWT payload.
     *
     * @param token the JWT token to decode
     * @return the role extracted from the token payload in uppercase
     */
    private String extractRoleFromJwt(String token) {
        try {
            String[] splitToken = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(splitToken[1]));
            if (payload.contains(ROLE_CLAIM)) {
                int startIndex = payload.indexOf(ROLE_CLAIM) + ROLE_CLAIM.length();
                int endIndex = payload.indexOf('"', startIndex + 1);
                return payload.substring(startIndex + 1, endIndex).toUpperCase();
            }
            throw new IllegalArgumentException("Role not found in token payload");
        } catch (Exception ex) {
            throw new RuntimeException("Error decoding token payload", ex);
        }
    }

    /**
     * Sets the authentication context with the user's role.
     *
     * @param request the HTTP request
     * @param role    the user's role
     */
    private void setAuthenticationContext(HttpServletRequest request, String role) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                null, null, List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role)));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
