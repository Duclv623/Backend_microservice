package microservice_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì đây là API stateless (không dùng session/form)
                .csrf(csrf -> csrf.disable())
                // Tạm thời cho phép tất cả request.
                // Sau này khi làm JWT: đổi thành yêu cầu xác thực và thêm JWT filter tại đây.
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
