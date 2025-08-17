package nghia_le.user_features.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class UserRes {
    private String id;
    private String email;
    private String roleId;
    private String name;
    public UserRes(String id, String email, String roleId, String name) {
        this.id = id;
        this.email= email;
        this.roleId= roleId;
        this.name= name;
    }
}
