package e_commerce.monolithic.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final  JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private static final String [] PUBLIC_ENDPOINTS = {
            "/api/users/login",
            "/api/users/register",
            "/api/admin/login",
            "/api/users/products/**",
            "/api/users/categories/**",
            "/uploads/**",

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF vì chúng ta sử dụng JWT (stateless)
                .cors(Customizer.withDefaults()) // Kích hoạt CORS (sử dụng cấu hình từ bean corsConfigurationSource)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sử dụng phiên làm việc stateless cho JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Cho phép truy cập công khai đến các endpoint này

                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Chỉ ADMIN mới có quyền truy cập các đường dẫn này
                        // Ví dụ: Người dùng hoặc admin đều có thể xem/cập nhật profile của họ
                        .requestMatchers("/api/user/profile/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/cart/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/orders/**").hasRole("USER")
                        .anyRequest().authenticated() // Tất cả các request khác yêu cầu xác thực
                )
                .exceptionHandling(exceptions -> exceptions
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Xử lý các lỗi xác thực (ví dụ: thiếu token, token không hợp lệ)
                        // .accessDeniedHandler(new CustomAccessDeniedHandler()) // Tùy chọn: Thêm AccessDeniedHandler để xử lý các lỗi ủy quyền (người dùng xác thực nhưng không có quyền)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm JWT filter trước filter mặc định của Spring Security

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
    // Cấu hình CORS toàn cục
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cần thay thế "*" bằng các origin cụ thể của frontend trong môi trường production
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://your-frontend-domain.com", "http://localhost:5173")); // Cho phép từ các origin cụ thể, hoặc Arrays.asList("*") cho tất cả (chỉ dev)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các phương thức HTTP được cho phép
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept")); // Các header được cho phép
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc HTTP authentication (nếu có)
        configuration.setMaxAge(3600L); // Thời gian cache cho pre-flight request

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình CORS này cho tất cả các đường dẫn
        return source;
    }
}
