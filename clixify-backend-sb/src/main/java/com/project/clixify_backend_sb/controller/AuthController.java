package com.project.clixify_backend_sb.controller;

import com.project.clixify_backend_sb.dtos.RegisterRequest;
import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")        //we have permitted the requests form this endpoint without having to be authenticated as it will have public pages like register, login, forgot password endpoints so it has to be public so that people can come and register or login (we configured this in security/WebSecurityConfig)
@AllArgsConstructor         //In modern Spring, we don't need to use @Autowired annotation to inject dependencies, we can use @AllArgsConstructor annotation to inject dependencies.
public class AuthController
{
    private UserService userService;

    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest)
    {
        //we are creating a new 'user' object which is of type 'User'(which is an Entity )and setting the values from the 'registerRequest' object which is a DTO (Data Transfer Object) of type 'RegisterRequest'
        //we are doing this because we want to save the user in the database and in database we have 'User' Entity.
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setRole("ROLE_USER");   //hardcoding the role for now, any register request will have role as "ROLE_USER" for now

        userService.registerUser(user);  //saving the user in the database through 'UserService' service layer.
        return ResponseEntity.ok("User Registered Successfully.");
    }
}
