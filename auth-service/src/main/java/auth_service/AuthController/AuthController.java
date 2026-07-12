package auth_service.AuthController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @GetMapping("/api/auth")
    public String authenticate() {
        return "Authentication successful";
    }
}
