package com.bichotas.moduloprestamos.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configures CORS settings for the application.
 * - Allowed Origins: Controlled via the `frontend.url` property.
 * - Allowed Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD.
 * - Allowed Headers: All headers are allowed.
 * - Credentials: Allowed to be included in requests.
 */
@Configuration
public class CorsConfig {


    private String frontendUrl = "*";

    /**
     * Configures a CORS filter bean to handle Cross-Origin Resource Sharing (CORS) requests.
     *
     * @return a {@link CorsFilter} configured with the specified CORS settings.
     * <p>
     * The CORS configuration allows:
     * - Credentials to be included in requests.
     * - Requests from the specified frontend URL.
     * - All headers to be included in requests.
     * - HTTP methods: GET, POST, PUT, DELETE, PATCH, OPTIONS, and HEAD.
     * - A maximum age of 3600 seconds (1 hour) for preflight requests.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(frontendUrl));
        config.addAllowedHeader("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        source.registerCorsConfiguration("/**", config);
        config.setMaxAge(3600L);
        return new CorsFilter(source);
    }
}
