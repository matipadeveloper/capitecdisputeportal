package za.co.capitec.capitecdisputeportalserver.services;

import za.co.capitec.capitecdisputeportalserver.requests.LoginRequest;

import java.util.Map;

public interface AuthService {
    Map<String, Object> authenticate(LoginRequest loginRequest);
}
