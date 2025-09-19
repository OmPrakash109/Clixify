package com.project.clixify_backend_sb.dtos;

import lombok.Data;

import java.util.Set;

@Data       //Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
public class RegisterRequest        //DTO(Data Transfer Object) to define structure of the request for user registration.
{
    private String email;
    private String username;
    private String password;
    private Set<String> role;
}
