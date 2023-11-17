
package MyProjects.Accede.security.jwt;

import MyProjects.Accede.security.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    @Value("${jwt.refreshMs}")
    private int jwtRefreshMs;

    public JwtDTO generateJwtToken(Authentication authentication, boolean rememberMe) {
        String authorities = (String)authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        String accessToken = Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtExpirationMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        String refreshToken = null;
        if (rememberMe) {
            refreshToken = Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtRefreshMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        }

        return new JwtDTO(accessToken, refreshToken);
    }

    private Key key() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String getUserNameFromJwtToken(String token) {
        return ((Claims)Jwts.parserBuilder().setSigningKey(this.key()).build().parseClaimsJws(token).getBody()).getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(this.key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException var3) {
            log.error("Invalid JWT token: {}", var3.getMessage());
        } catch (ExpiredJwtException var4) {
            log.error("JWT token is expired: {}", var4.getMessage());
        } catch (UnsupportedJwtException var5) {
            log.error("JWT token is unsupported: {}", var5.getMessage());
        } catch (IllegalArgumentException var6) {
            log.error("JWT claims string is empty: {}", var6.getMessage());
        }

        return false;
    }

    public JwtDTO generateJwtTokenWith2Fact(String username) {
        String accessToken = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtExpirationMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        return new JwtDTO(accessToken, (String)null);
    }
}
