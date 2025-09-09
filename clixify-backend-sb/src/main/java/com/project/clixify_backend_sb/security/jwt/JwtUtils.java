package com.project.clixify_backend_sb.security.jwt;

//JwtUtils.java is a service layer for JwtAuthenticationFilter.java.

import com.project.clixify_backend_sb.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils
{
    //Injecting JWT Secret key and JWT expiration duration (from application.properties). Good practice to declare any sensitive info like keys and duration in application.properties and then injecting wherever to be used.
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.expiration.inMs}")
    private long jwtExpirationMs;

    //Method to extract JWT token from Authorization header of the request (Authorization Header -> Bearer <Token>)
    public String getJwtFromHeader(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");     //first extracting authorization header from request
        if(authHeader != null && authHeader.startsWith("Bearer "))      //if auth header is not null and has 'Bearer' token in it.
        {
            return authHeader.substring(7);                 //return the bearer token (bearer token starts from 7th index till last as 'Bearer <Token>')
        }
        return null;                                                    //otherwise return null;
    }


    //Method to Generate JWT tokens using 'UserDetailsImpl' object (which is spring compatible representation of User, to which we converted our 'User' object into)
    public String generateToken(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        // convert roles into a comma-separated string
        String roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()                                                           //.builder() as we are building the token
                .subject(username) // standard JWT claim: subject = user’s identity
                .claim("roles", roles) // custom claim: user roles
                .issuedAt(new Date()) // iat (issued-at timestamp)
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // exp (expiration timestamp)
                .signWith(key()) // sign with secret/private key
                .compact(); // final step: serialize to compact JWT string
    }

    /*
    - In 'generateToken(UserDetails, userDetails)' method, user details in methods (or just claims) like .subject(), .claim() and token detains in methods (or just claims) like .issuedAt(), .expiration() are used to create the 'Payload' part of the JWT token.
    - The .signWith() method generates the 'Signature' part (based on Header + Payload + secret key).
    ('Header' part is automatically generated when we call .signWith(key()), jjwt automatically sets the 'alg' (algorithm) field in the header.)
    - Finally, .compact() serializes the Header, Payload, and Signature into the standard
    JWT string format:
        Base64UrlEncode(Header) + "." + Base64UrlEncode(Payload) + "." + Base64UrlEncode(Signature)

    This compact string is the actual token that can be sent in an HTTP Authorization header.
    */


    //Method to generate sign-in key that is used in token generation - The resulting key is used for both signing new tokens and verifying existing ones
    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));    //applying HMAC-SHA-256 on secret key after decoding it into Base 64.
    }



    //Method to parse the JWT token and extract the username from it
    public String getUserNameFromJwtToken(String token)
    {
        return Jwts.parser().verifyWith((SecretKey) key())          //.parser() as we are parsing the token for extracting username from it
                .build().parseSignedClaims(token)
                .getPayload().getSubject();     //extracting 'subject' which has 'username' (as mentioned in method for generating token) from the 'Payload' part of the JWT token.
    }


    //Method to validate the JWT token. (returns boolean value)
    public boolean validateToken(String token)
    {
        try {
            Jwts.parser().verifyWith((SecretKey) key())         //.parser() as we are parsing the token for validating ti.
                    .build().parseSignedClaims(token);
            return true;                                        //returning true if token is valid or else throwing exception.
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
