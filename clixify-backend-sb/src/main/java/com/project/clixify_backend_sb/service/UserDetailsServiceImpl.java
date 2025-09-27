package com.project.clixify_backend_sb.service;

/*
We created the 'UserDetailsServiceImpl' class (by implementing 'UserDetailsService interface) as we have our own custom representation of user and we might be storing it in any database like MySQL, MongoDB etc. depending on our requirement.
                         so we created this so that Spring Security can understand how to load the user details from the database for our custom user representation.
                         In our 'UserDetailsServiceImpl', We override the 'loadUserByUsername' method to load the user details from the database.
                         This class helps to load user details from the database and convert it to UserDetailsImpl object (by UserDetailsImpl.build(user)).
*/

import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.build(user);     //after loading the user from the database, we convert it into UserDetailsImpl object (by UserDetailsImpl.build(user)) so that spring security can work with the user.
    }
}

//Spring security knows as how to authenticate the user for our application because it is aware of the 'UserDetailsServiceImpl' class and 'UserDetailsImpl' class that we created.
