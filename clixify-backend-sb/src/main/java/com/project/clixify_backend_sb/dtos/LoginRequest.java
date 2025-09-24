package com.project.clixify_backend_sb.dtos;

import lombok.Data;

@Data
public class LoginRequest
{
    //For login, user should provide just username and password and not other fields that user has to provide while registering.
    private String username;
    private String password;
}
