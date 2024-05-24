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
     //EXTRAE UN CLAIM EN PARTICULAR (SE LE PASA EL TOKEN Y UNA FUNCION EN PARTICULAR DE LA INTERFAZ DE CLAIMS)
    public <T> T extractClaim (String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);  //EXTRAIGO TODOS LOS CLAIMS DEL TOKEN
        return claimsTFunction.apply(claims); // RETORNA UN CLAIM EN PARTICULAR
    }

    //EXTRAIGO EL NOMBRE DE USUARIO (MAIL)
    public String extractUserName (String token) {
        return extractClaim(token, Claims :: getSubject);
    }

    //EXTRAIGO LA FECHA DE EXPIRACION
    public Date extractExpiration (String token) {
        return extractClaim(token, Claims :: getExpiration);
    }

    //
    public Boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date()); //SI LA FECHA DE EXP ESTA ANTES QUE LA FECHA DE AHORA. TRUE
    }

    //CREA EL TOKEN (RECIBE EL MAP Y EL USERNAME DE USERDETAILS)
    private String createToken (Map<String, Object> claims, String username) {
        return Jwts
                .builder() //INICIA UN OBJ DE EL TIPO JWT
                .claims(claims) //LE PASO LOS CLAIMS QUE RECIBO POR PARAMETRO (EL MAP)
                .subject(username) //LE PASO EL USERNAME (LO QUE QUIERO QUE SEA EL SUB EN EL PAYLOAD)
                .issuedAt(new Date(System.currentTimeMillis())) //LA FECHA DE EMISION
                .expiration(new Date(System.currentTimeMillis() + EXPIRATON_TOKEN)) // LA FECHA DE EXP
                .signWith(SECRET_KEY) //USO LA SECRETKEY PARA FIRMAR EL TOKEN QUE ESTOY GENERANDO
                .compact(); //CONSTRUYO EL TOKEN JWT COMPLETO Y LO DEVOLVEMOS COMO UN STRING
    }

    //RECIBE COMO PARAMETRO UN USERDETAILS (EL USER QUE CREE EN USERDETAILSSERVICEIMPL)
    public String generateToken (UserDetails userDetails) {
        //GENERO UN MAP DE STRING OBJECT
        Map<String, Object> claims = new HashMap<>(); //ASOCIO UNA CLAVE A UN VALOR
        String rol = userDetails
                .getAuthorities() //OBTENGO LAS AUTORIDADES
                .iterator() //CONVIERTO LA COLECCION DE AUTORIDADES EN UN ITERADOR PARA RECORRER LA COLECCION
                .next() //DEVUELVO EL SIGUIENTE ELEM DE LA ITERACION
                .getAuthority(); //OBTENGO LA AUTORIDAD (EL ROL CLIENT)
        claims.put("rol", rol); //AGREGO EL ROL OBTENIDO AL MAP DE STRING OBJECT
        return createToken(claims, userDetails.getUsername()); //GENERO TOKEN
    }
}