package com.project.clixify_backend_sb.service;

import com.project.clixify_backend_sb.dtos.LoginRequest;
import com.project.clixify_backend_sb.repository.UserRepository;
import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.security.jwt.JwtAuthenticationResponse;
import com.project.clixify_backend_sb.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@AllArgsConstructor     //In modern Spring, we don't need to use @Autowired annotation to inject dependencies, we can use @AllArgsConstructor annotation to inject dependencies.
public class UserService
{
    private PasswordEncoder passwordEncoder;    //password encoder to encode the password before saving it in the database.
    private UserRepository userRepository;      //user repository to save the user in the database.
    private AuthenticationManager authenticationManager;    //authentication manager to authenticate the user.
    private JwtUtils jwtUtils;                              // jwtUtils to generate the JWT token.
    //Whenever we inject dependencies, we keep the constructor private so that we can't create an object of this class from outside.

    //Business logic for user registration
    public User registerUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //before saving the user, we need to encode the password
        return userRepository.save(user);   //saving the user in the database through 'UserRepository' repository layer.- saving the user in the database.
    }


    //Business logic for user login where we perform authentication - and as we have to return the JWT token in the response(on successful login) so that we can authenticate the user for follow-up requests, so we will return the JWT token in the response with the help of 'JwtAuthenticationResponse' class. (Unlike registerUser Service where we return the user object as response)
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest)
    {
        //1. First the user is authenticated by passing the username and password (wrapped in 'UsernamePasswordAuthenticationToken' object) to the 'authenticate' method of 'AuthenticationManager' class which will return an 'Authentication' object if the authentication is successful and throw an exception if the authentication fails.
        //When we call authenticationManager.authenticate, Spring Security takes the UsernamePasswordAuthenticationToken (which contains just username/password) and Internally loads the full user details (including roles, enabled status, etc.) from your UserDetailsServiceImpl and Returns a fully populated Authentication object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        //2. Update the Security Context Holder with the authentication object so that it can be used for authorization.
        SecurityContextHolder.getContext().setAuthentication(authentication);       //setting the security context means, the spring security construct will hold the authentication data for the current request/session
        // 3. Extract user details from the authentication object as for token generation we need user details
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();       //this 'authentication' object is fully loaded with user details (including roles, enabled status, etc.) that were fetched during authentication
        // 4. Generate JWT token using the user details, and this JWT token will be used for authentication of the user for follow-up requests.
        String jwt = jwtUtils.generateToken(userDetails);
        // 5. Return the JWT token in the response wrapped in 'JwtAuthenticationResponse' object.
        return new JwtAuthenticationResponse(jwt);
    }
    //Business logic for finding user/retrieving user details by username (to get the user details from the principal object) while generating the short URL
    //As every short URL in the database is mapped to a user so we need to get the user details from the principal object to map the short URL to that user.
    public User findByUsername(String name)
    {
        return userRepository.findByUsername(name).orElseThrow(             //retrieves the user details with the help of userRepository and throws an exception if the user is not found
                () -> new UsernameNotFoundException("User not found with username: " + name)
        );
    }
}
