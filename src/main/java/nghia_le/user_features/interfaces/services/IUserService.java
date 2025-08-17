package nghia_le.user_features.interfaces.services;

import nghia_le.user_features.DTOs.AuthRes;
import nghia_le.user_features.DTOs.RegisterUserReq;
import nghia_le.user_features.DTOs.UserRes;

import java.util.List;

public interface  IUserService {
    List<UserRes> getAllUsers();
    AuthRes registerUser(RegisterUserReq req);
}
