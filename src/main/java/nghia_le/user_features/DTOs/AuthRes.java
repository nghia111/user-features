package nghia_le.user_features.DTOs;

import lombok.Data;

@Data
public class AuthRes {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public AuthRes(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public AuthRes(){};
}
