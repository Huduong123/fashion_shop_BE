package e_commerce.monolithic.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import e_commerce.monolithic.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/admin/login",
            "/api/user/login",
            "/api/user/register");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);
                var userOpt = userService.findByUsername(username);
                if (userOpt.isPresent()) {
                    var user = userOpt.get();
                    // Eagerly load authorities
                    var authorities = user.getAuthorities().stream()
                            .map(auth -> auth.getAuthority())
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, authorities.stream()
                                    .map(auth -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                            auth))
                                    .collect(Collectors.toList()));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
