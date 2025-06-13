package com.example.user_management_service.JWTSecurityConfiguration;

import com.example.user_management_service.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "98a14ab0db3e500008fd50122294d30ac3831a1658e93ce8e86783c0dc58337c"; //am generat o cu un tool online, este o cheie de 256 biti

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public String generateToken(UserDetails userDetails){
        //userDetails contine doat getUsername() si getPassword()
        return generateToken(new HashMap<>(), userDetails);
    }

//    public String generateToken(UserDetails userDetails) {
//        if (userDetails instanceof User) {
//            User user = (User) userDetails;
//            Map<String, Object> extraClaims = new HashMap<>();
//            extraClaims.put("userId", user.getId().toString());
//            extraClaims.put("userRole", user.getRole());
//
//            return generateToken(extraClaims, userDetails);
//        } else {
//            // Tratează cazul în care userDetails nu este de tipul User
//            throw new IllegalArgumentException("userDetails trebuie sa fie de tipul User");
//        }
//    }

    public String generateTokenLogin(User user) {
        //user contine toate campurile inclusiv getId(), getRole()
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("userRole", user.getRole());

        return generateToken(extraClaims, user);
    }

    public String generateTokenRegister(User user){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId().toString());

        return generateToken(extraClaims, user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) //tokenul va fi valabil pentru 24 de ore
                //.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)) //tokenul va fi valabil pentru 30 de zile
                .setExpiration(new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
