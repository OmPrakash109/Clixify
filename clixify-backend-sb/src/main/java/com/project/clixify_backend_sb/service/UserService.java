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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor     //In modern Spring, we don't need to use @Autowired annotation to inject dependencies, we can use @AllArgsConstructor annotation to inject dependencies.
public class UserService
{
    private PasswordEncoder passwordEncoder;    //password encoder to encode the password before saving it in the database.
    private UserRepository userRepository;      //user repository to save the user in the database.
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    //Whenever we inject dependencies, we keep the constructor private so that we can't create an object of this class from outside.

    //Business logic for user registration
    public User registerUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //before saving the user, we need to encode the password
        return userRepository.save(user);   //saving the user in the database through 'UserRepository' repository layer.- saving the user in the database.
    }

    //Business logic for user login - and as we have to return the JWT token in the response(on successful login) so that we can authenticate the user for follow-up requests, so we will return the JWT token in the response with the help of 'JwtAuthenticationResponse' class. (Unlike registerUser Service where we return the user object as response)
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }
}
