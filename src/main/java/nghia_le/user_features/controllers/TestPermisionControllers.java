package nghia_le.user_features.controllers;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestPermisionControllers {
    @GetMapping("all")
    String allowAll() {
        return "allowAll";
    }
    @GetMapping("admin")
    String allowAdmin() {
        return "admin";
    }
    @GetMapping("login")
    String allowLogin() {
        return "allowLogin";
    }

}

