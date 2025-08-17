package nghia_le.user_features.DTOs;

import lombok.Data;

@Data // idk why use this but dont have getter and setter
public class RegisterUserReq {
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getName() {
        return name;
    }

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String password;
    private String confirmPassword;
    private String name;
}
