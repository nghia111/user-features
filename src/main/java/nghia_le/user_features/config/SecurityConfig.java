package nghia_le.user_features.config;

import jakarta.servlet.http.HttpServletResponse;
import nghia_le.user_features.middlewares.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF cho tất cả các endpoint.
                // Điều này rất quan trọng đối với các REST API không sử dụng form HTML.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả các yêu cầu POST tới "/users"
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        // Cho phép tất cả các yêu cầu tới các đường dẫn liên quan đến Swagger
                        .requestMatchers(
                                "/test/all",
                        // "/users/**", this is allowance for anonymous
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/test/admin").hasRole("EC2B405F-72E1-4EC0-A196-3AEB96B2F0E1")

                        // Yêu cầu xác thực cho tất cả các yêu cầu khác
                        .anyRequest().authenticated()
                )
                // Stateless session (REST API không dùng session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Chèn filter JWT trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Lỗi 401: Xác thực thất bại (ví dụ: không có token)
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Lỗi 403: Được xác thực nhưng không có quyền truy cập
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden\"}");
                        })
                );

        return http.build();
    }
}