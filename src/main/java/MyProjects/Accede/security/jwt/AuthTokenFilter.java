
package MyProjects.Accede.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import MyProjects.Accede.security.services.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = this.parseJwt(request);
            if (jwt != null && this.jwtUtils.validateJwtToken(jwt)) {
                String username = this.jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                authentication.setDetails((new WebAuthenticationDetailsSource()).buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User {} successfully authenticated", username);
            }
        } catch (Exception e) {
            logger.error("Cannot set User authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        return StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ") ? headerAuth.substring(7) : null;
    }
}
