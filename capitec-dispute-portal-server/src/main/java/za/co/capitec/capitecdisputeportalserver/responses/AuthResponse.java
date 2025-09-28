package za.co.capitec.capitecdisputeportalserver.responses;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private LocalDateTime loginTime;
}
