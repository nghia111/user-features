package nghia_le.user_features.controllers;

import jakarta.annotation.security.PermitAll;
import nghia_le.user_features.DTOs.AuthRes;
import nghia_le.user_features.DTOs.RegisterUserReq;
import nghia_le.user_features.DTOs.UserRes;
import nghia_le.user_features.common.ApiResponse;
import nghia_le.user_features.interfaces.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllers {
    private final IUserService iUserService;
    public UserControllers(IUserService _iUserservice){
        iUserService = _iUserservice;
    }
    @GetMapping
//    @PermitAll
    ApiResponse<List<UserRes>> getAllUsers(){
        var result = iUserService.getAllUsers();
        return ApiResponse.success(result);
    }
    @PostMapping
//    @PermitAll
    ApiResponse<AuthRes> registerUser(@RequestBody RegisterUserReq req){
        var result = iUserService.registerUser(req);
        return ApiResponse.success(result);
    }
}
