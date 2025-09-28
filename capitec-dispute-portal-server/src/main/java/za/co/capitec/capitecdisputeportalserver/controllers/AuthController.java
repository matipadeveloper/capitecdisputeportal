package za.co.capitec.capitecdisputeportalserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.capitecdisputeportalserver.requests.LoginRequest;
import za.co.capitec.capitecdisputeportalserver.services.AuthService;
import za.co.capitec.capitecdisputeportalserver.utils.ValidatorUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token for API access")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        ValidatorUtil.validateLoginRequest(loginRequest);
        try {
            Map<String, Object> loginResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
