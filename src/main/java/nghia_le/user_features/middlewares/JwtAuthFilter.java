package nghia_le.user_features.middlewares;

import nghia_le.user_features.utils.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;
            String role = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    username = jwtUtil.getUsernameFromToken(token);
                    // Giả sử JwtUtil của bạn có phương thức getRoleFromToken(token)
                    // và phương thức này trả về tên vai trò (ví dụ: "EC2B405F-72E1-4EC0-A196-3AEB96B2F0E1")
                    // Tuy nhiên, để làm việc với hasRole, chúng ta cần thêm tiền tố "ROLE_"
                    // Điều này cần được xử lý trong JwtUtil hoặc ở đây.
                    role = "ROLE_" + jwtUtil.getRoleFromToken(token);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Tạo một GrantedAuthority với tiền tố "ROLE_"
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                // Tạo đối tượng User của Spring Security
                User userDetails = new User(
                        username,
                        "{noop}", // Mật khẩu không quan trọng ở đây, chỉ là placeholder
                        authorities // Truyền danh sách quyền vào đây
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Log lỗi để debug
            System.err.println("Lỗi trong JwtAuthFilter: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
