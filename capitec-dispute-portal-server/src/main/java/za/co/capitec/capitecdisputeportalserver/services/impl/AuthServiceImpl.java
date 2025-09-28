package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import za.co.capitec.capitecdisputeportalserver.requests.LoginRequest;
import za.co.capitec.capitecdisputeportalserver.services.AuthService;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> authenticate(LoginRequest loginRequest) {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("username", loginRequest.getUsername());
            formData.add("password", loginRequest.getPassword());
            formData.add("scope", "openid profile email");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenResponse = response.getBody();

                // Validate required fields exist
                Object accessToken = tokenResponse.get("access_token");
                if (accessToken == null) {
                    throw new RuntimeException("Access token not received from authentication server");
                }

                return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", tokenResponse.getOrDefault("refresh_token", ""),
                    "expiresIn", tokenResponse.getOrDefault("expires_in", 3600),
                    "tokenType", "Bearer",
                    "username", loginRequest.getUsername(),
                    "loginTime", LocalDateTime.now()
                );
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
