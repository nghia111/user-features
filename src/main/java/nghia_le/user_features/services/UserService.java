package nghia_le.user_features.services;

import nghia_le.user_features.DTOs.AuthRes;
import nghia_le.user_features.DTOs.RegisterUserReq;
import nghia_le.user_features.DTOs.UserRes;
import nghia_le.user_features.interfaces.repositories.IUserRepository;
import nghia_le.user_features.interfaces.services.IUserService;
import nghia_le.user_features.models.User;
import nghia_le.user_features.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private IUserRepository userRepository;
    public UserService(IUserRepository _userRepository, PasswordEncoder _passwordEncoder, JwtUtil _jwtUtil){
        jwtUtil = _jwtUtil;
        passwordEncoder = _passwordEncoder;
        userRepository = _userRepository;
    }
    @Override
    public List<UserRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserRes> results = users.stream()
                .map(user -> new UserRes(
                        user.getId(),
                        user.getEmail(),
                        user.getRoleId(),
                        user.getName()
                ))
                .toList(); // Java 16+ dùng toList(), Java 8 dùng collect(Collectors.toList())
        return results;
    }
    @Override
    public AuthRes registerUser(RegisterUserReq req){
        // 1. Check email existed
        if(!req.getPassword().equals(req.getConfirmPassword())){
            throw new RuntimeException("Mật khẩu không khớp!");
        }
        User existing = userRepository.findByEmail(req.getEmail());
        if (existing != null) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        // 2. Create new user
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(req.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(req.getPassword())); // mã hoá password
        newUser.setRoleId("A9CE0E87-C54C-4E0C-99FE-1346D42BD32A"); // mặc định gán role USER

        // 3. save Db
        userRepository.create(newUser);
        // 4. generate token

        String accessToken = jwtUtil.generateAccessToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(newUser.getEmail())
                        .password("dummyPassword")
                        .roles(newUser.getRoleId())
                        .authorities(List.of(new SimpleGrantedAuthority(newUser.getRoleId())))
                        .build()
        );
        String refreshToken = jwtUtil.generateRefreshToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(newUser.getEmail())
                        .password("dummyPassword")
                        .roles(newUser.getRoleId())
                        .authorities(List.of(new SimpleGrantedAuthority(newUser.getRoleId())))
                        .build()
        );
        return new AuthRes(accessToken, refreshToken);
    }
}