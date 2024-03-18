
package MyProjects.Accede.security.jwt;

import MyProjects.Accede.security.dto.JwtDTO;
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

    public JwtDTO generateJwtToken(Authentication authentication, boolean rememberMe) //method for generating Jwt
    {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")); //Setting Granted Authorities into a string
        String accessToken = Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtExpirationMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        //building access token using jwt builder. Setting expiration timer on token in milliseconds and signed with a signature key
        String refreshToken = null;
        if (rememberMe) //if rememberMe was selected in sign in, it creates a refreshToken that extends the time the user details stay remembered
        {
            refreshToken = Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtRefreshMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        }
        return new JwtDTO(accessToken, refreshToken);
    }

    private Key key() //key signature for encryption of tokens using HS512 algorithm for its generation
    {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String getUserNameFromJwtToken(String token) //extracting username from jwt
    {
        return (Jwts.parserBuilder().setSigningKey(this.key()).build().parseClaimsJws(token).getBody()).getSubject();
    }

    public boolean validateJwtToken(String authToken) //try and catch block for jwt parse builder exceptions
    {
        try {
            Jwts.parserBuilder().setSigningKey(this.key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException InvalidTokenException) {
            log.error("Invalid JWT token: {}", InvalidTokenException.getMessage());
        } catch (ExpiredJwtException ExpiredTokenException) {
            log.error("JWT token is expired: {}", ExpiredTokenException.getMessage());
        } catch (UnsupportedJwtException UnsupportedTokenException) {
            log.error("JWT token is unsupported: {}", UnsupportedTokenException.getMessage());
        } catch (IllegalArgumentException StringEmptyClaimException) {
            log.error("JWT claims string is empty: {}", StringEmptyClaimException.getMessage());
        }
        return false;
    }

    public JwtDTO generateJwtTokenWith2Fact(String username) //generating jwt token with 2-factor authentication
    {
        String accessToken = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + (long)this.jwtExpirationMs)).signWith(this.key(), SignatureAlgorithm.HS512).compact();
        return new JwtDTO(accessToken, null);
    }
}
