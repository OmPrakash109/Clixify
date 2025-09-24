package com.project.clixify_backend_sb.security.jwt;

//This is a DTO(Data Transfer Object) class which represents the  JWT authentication response.


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse
{
    private String token;
}
