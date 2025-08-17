package nghia_le.user_features.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;
    private String passwordHash;
    private String roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getRoleId() { return roleId; }
    public String getName(){ return name; }

}

