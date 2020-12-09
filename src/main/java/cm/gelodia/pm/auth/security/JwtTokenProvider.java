package cm.gelodia.pm.auth.security;


import cm.gelodia.pm.auth.payload.SignInResponse;
import cm.gelodia.pm.commons.exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private KeyStore keyStore;

    @Value("${app.accessJwtExpirationInMs}")
    private int accessJwtExpirationInMs;

    @Value("${app.refreshJwtExpirationInMs}")
    private int refreshJwtExpirationInMs;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/jwt.jks");
            keyStore.load(resourceAsStream, "Azerty!123".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new ApplicationException("Error occurred when loading keystore file");
        }
    }

    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String,Object> claims = new HashMap<>();
        claims.put("authorities", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("firstName", userPrincipal.getFirstName() != null? userPrincipal.getFirstName(): "");
        claims.put("lastName", userPrincipal.getLastName());
        claims.put("username", userPrincipal.getUsername());
        claims.put("email", userPrincipal.getEmail());
        claims.put("mobile", userPrincipal.getMobile() != null? userPrincipal.getMobile(): "");
        claims.put("city", userPrincipal.getCity() != null? userPrincipal.getCity(): "");
        claims.put("company_id", userPrincipal.getCompany().getId());
        claims.put("company_name", userPrincipal.getCompany().getName());

        Date now =  new Date();
        Date expiryDate = new Date(now.getTime() + accessJwtExpirationInMs);
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(claims)
               // .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("jwt", "Azerty!123".toCharArray());
        }catch (KeyStoreException | NoSuchAlgorithmException  | UnrecoverableKeyException e) {
            throw new ApplicationException("Error occurred when retrieving private key from keystore");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("jwt").getPublicKey();
        } catch (KeyStoreException e) {
            throw new ApplicationException("Error occured while retrieving public key from keystore");
        }
    }

    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now =  new Date();
        Date expiryDate = new Date(now.getTime() + refreshJwtExpirationInMs);
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                //.signWith(SignatureAlgorithm.HS512, jwtSecret)
                .signWith(getPrivateKey())
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public SignInResponse getUserFromToken(String accessToken, String refreshToken) {
        SignInResponse signInResponse = new SignInResponse();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(accessToken)
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
        signInResponse.setAccessToken(accessToken);
        signInResponse.setRefreshToken(refreshToken);
        signInResponse.setAuthorities(
                Arrays.asList(claims.get("authorities")
                        .toString()
                        .replaceAll("^\\[|]$|[\\s]", "") //remove brackets & white space
                        .split(",")));
        return signInResponse;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
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
