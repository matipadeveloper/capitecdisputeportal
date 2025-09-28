package za.co.capitec.capitecdisputeportalserver.responses;

import lombok.Data;
import java.util.List;

@Data
public class UserInfoResponse {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private List<String> roles;
}
