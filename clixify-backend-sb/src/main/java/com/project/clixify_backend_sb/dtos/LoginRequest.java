package com.project.clixify_backend_sb.dtos;

import lombok.Data;

@Data
public class LoginRequest    //DTO (Data Transfer Object) for login request
{
    //For login, user should provide just username and password and not other fields that user had to provide while registering.
    private String username;
    private String password;
}
