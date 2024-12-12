package com.bichotas.moduloprestamos.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

/**
 * ApiClient is responsible for communicating with the API Gateway to validate JWT tokens.
 */
@Component
@RequiredArgsConstructor
public class ApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);
    private static final String APIGATEWAY_URL = "https://zw8dshmxwa.execute-api.us-east-1.amazonaws.com/BiblioSoft/";

    private final RestClient restClient;

    /**
     * Validates the provided JWT token by sending a request to the API Gateway.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            ResponseEntity<Map> response = restClient.get()
                    .uri(APIGATEWAY_URL + "auth/session")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.warn("Token validation failed with status: {}", response.getStatusCode());
                return false;
            }

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "401".equals(String.valueOf(responseBody.get("statusCode")))) {
                LOGGER.warn("Token validation returned unauthorized status");
                return false;
            }

            return true;
        } catch (Exception e) {
            LOGGER.error("Error validating token: {}", e.getMessage(), e);
            return false;
        }
    }
}