package com.mindhub.homebanking.servicesSecurity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;

@Service
public class JwtUtilService {
    //CLAVE SECRETA PARA FIRMAR Y VERIFICAR EL TOKEN JWT
    private static final SecretKey SECRET_KEY =Jwts.SIG.HS256.key().build();

    //TIEMPO DE EXPIRACION DEL TOKEN EN MILISEGUNDOS(DURA 1H)
    private static final long EXPIRATON_TOKEN = 1000 * 60 * 60;

    //'EXTRAE TODOS LOS CLAIMS DEL TOKEN JWT
    public Claims extractAllClaims (String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUserName (String token) {
        return extractClaim(token, Claims :: getSubject);
    }

    public Date extractExpiration (String token) {
        return extractClaim(token, Claims :: getExpiration);
    }

    public Boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date());
    }

    private String createToken (Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATON_TOKEN))
                .signWith(SECRET_KEY)
                .compact(); //CONSTRUYO EL TOKEN JWT COMPLETO Y LO DEVOLVEMOS COMO UN STRING
    }

    public String generateToken (UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }

}