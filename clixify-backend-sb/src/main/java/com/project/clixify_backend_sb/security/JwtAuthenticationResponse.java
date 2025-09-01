package com.project.clixify_backend_sb.security;

//This is a DTO(Data Transfer Object) class which represents the  JWT authentication response.


import lombok.Data;

@Data
public class JwtAuthenticationResponse
{
    private String token;
}
