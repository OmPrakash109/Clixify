package com.project.clixify_backend_sb.service;

/*
We create the UserDetailsImpl class (by implementing UserDetails interface) to bridge the gap/integrate our own custom representation of User with the Spring Security-compatible representation of User,
so that spring can understand what user to perform authentication and authorization on.
*/

import com.project.clixify_backend_sb.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;     //GrantedAuthority is about how roles are being managed in Spring Security (as to tellSpring Security about role of the user, it would need an object of type GrantedAuthority wherein we pass our custom role as parameter)

    public UserDetailsImpl(Long id, String email, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

//We made this 'build' method which would help us build the instance/object of the UserDetailsImpl
//This purpose of this build method is to convert our custom 'User' object (taken as parameter) from our database, into 'UserDetailsImpl' object (returned) so that spring security can work with the user.
     public static UserDetailsImpl build(User user)     //In parameter, we select parameter "User" of type that we created in 'models'
     {
         GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());   //extracts the role from the 'user' object and converts it into GrantedAuthority as Spring Security needs an object of GrantedAuthority to know the role of the user.
         return new UserDetailsImpl(    //returning the object of UserDetailsImpl
                 user.getUser_id(),
                 user.getEmail(),
                 user.getUsername(),
                 user.getPassword(),
                 Collections.singletonList(authority)
         );
     }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }
}
