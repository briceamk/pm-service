package cm.gelodia.pm.auth.security;


import cm.gelodia.pm.auth.payload.SignInResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String,Object> claims = new HashMap<>();
        claims.put("authorities", userPrincipal.getAuthorities().toArray());
        claims.put("firstName", userPrincipal.getFirstName() != null? userPrincipal.getFirstName(): "");
        claims.put("lastName", userPrincipal.getLastName());
        claims.put("username", userPrincipal.getUsername());
        claims.put("email", userPrincipal.getEmail());
        claims.put("mobile", userPrincipal.getMobile() != null? userPrincipal.getMobile(): "");
        claims.put("city", userPrincipal.getCity() != null? userPrincipal.getCity(): "");
        claims.put("company_id", userPrincipal.getCompany().getId());
        claims.put("company_name", userPrincipal.getCompany().getName());

        Date now =  new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .addClaims(claims)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public SignInResponse getUserFromToken(String token) {
        SignInResponse signInResponse = new SignInResponse();
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        signInResponse.setId(claims.getSubject());
        signInResponse.setFirstName(claims.get("firstName").toString());
        signInResponse.setLastName(claims.get("lastName").toString());
        signInResponse.setUsername(claims.get("username").toString());
        signInResponse.setEmail(claims.get("email").toString());
        signInResponse.setCity(claims.get("city").toString());
        signInResponse.setMobile(claims.get("mobile").toString());
        signInResponse.setCompanyId(claims.get("company_id").toString());
        signInResponse.setCompanyName(claims.get("company_name").toString());
        signInResponse.setAccessToken(token);
        return signInResponse;

    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
