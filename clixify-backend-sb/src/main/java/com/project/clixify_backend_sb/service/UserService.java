package com.project.clixify_backend_sb.service;

import com.project.clixify_backend_sb.repository.UserRepository;
import com.project.clixify_backend_sb.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor     //In modern Spring, we don't need to use @Autowired annotation to inject dependencies, we can use @AllArgsConstructor annotation to inject dependencies.
public class UserService
{
    private PasswordEncoder passwordEncoder;    //password encoder to encode the password before saving it in the database.
    private UserRepository userRepository;      //user repository to save the user in the database.


    public User registerUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //before saving the user, we need to encode the password
        return userRepository.save(user);   //saving the user in the database through 'UserRepository' repository layer.- saving the user in the database.
    }
}
