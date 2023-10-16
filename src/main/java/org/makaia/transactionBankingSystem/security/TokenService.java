package org.makaia.transactionBankingSystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Service
public class TokenService {
    @Value("${api.security.secret}")
    private String apiSecret;

    public String generateToken(User user) throws ApiException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("MAKAIA bank")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            List<String> error = List.of(exception.getMessage());
            throw new ApiException(500, error);
        }
    }

    public String getSubject(String token){
        if(token == null){
            throw new RuntimeException("Token nulo.");
        }
        DecodedJWT verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
             verifier = JWT.require(algorithm)
                    .withIssuer("MAKAIA bank")
                     .build()
                     .verify(token);

            verifier.getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException(exception.getMessage());
        }

        if(verifier.getSubject() == null){
            throw new RuntimeException("- Error. Verifier invalido.");
        }
        return verifier.getSubject();
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-05:00"));
    }
}
