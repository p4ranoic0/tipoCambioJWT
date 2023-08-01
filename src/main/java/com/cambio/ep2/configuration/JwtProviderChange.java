package com.cambio.ep2.configuration;


import com.cambio.ep2.convertidor.ConvertidorDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProviderChange {

    private final byte[] signinKey;
    private final String SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    private final long tokenValidityInSeconds;
    private final String apiKey = "d206438c-265d-411c-ba18-3b26687d82e6";

    public JwtProviderChange(){
        this.signinKey = Decoders.BASE64.decode(SECRET_KEY);
        this.tokenValidityInSeconds=500;
    }

    public String generatedJWTChange(String from,String to,String amount){

        Claims claims = new DefaultClaims();
        claims.put("api-key",apiKey);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+tokenValidityInSeconds*1000))
                .signWith(Keys.hmacShaKeyFor(signinKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public ConvertidorDTO readTokenChange(String token){
        Jws<Claims> claimsJws= Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        return ConvertidorDTO.builder()
                .from(claimsJws.getBody().get("from").toString())
                .to(claimsJws.getBody().get("to").toString())
                .amount(Double.valueOf(claimsJws.getBody().get("amount").toString()))
                .build();
    }

}


