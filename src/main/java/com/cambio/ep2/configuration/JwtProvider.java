package com.cambio.ep2.configuration;


import com.cambio.ep2.convertidor.ConvertidorDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final byte[] signinKey;
    private final String SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    private final long tokenValidityInSeconds;
    private final String apiKey = "d206438c-265d-411c-ba18-3b26687d82e6";

    public JwtProvider(){
        this.signinKey = Decoders.BASE64.decode(SECRET_KEY);
        this.tokenValidityInSeconds=180;
    }

    public String generatedJWT(String from,String to){

        Claims claims = new DefaultClaims();
        claims.put("api-key",apiKey);
        claims.put("from",from);
        claims.put("to",to);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenValidityInSeconds*1000))
                .signWith(Keys.hmacShaKeyFor(signinKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public ConvertidorDTO readToken(String token){
        Jws<Claims> claimsJws= Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        return ConvertidorDTO.builder()
                .from(claimsJws.getBody().get("from").toString())
                .to(claimsJws.getBody().get("to").toString())
                .build();
    }

}


