package com.project.clixify_backend_sb.security.jwt;

import com.project.clixify_backend_sb.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter       //JwtAuthenticationFilter' extends 'OncePerRequestFilter' to make sure each request is processed only once and checks the validity of the token for each request.
{
    @Autowired
    private JwtUtils jwtUtils;      //Injected the 'JwtUtils' bean to use its methods for extracting the JWT token from the Authorization Header of the incoming Request and validating it.

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;  //we use 'UserDetailsServiceImpl' instead of 'UserDetailsService' as we have implemented our own 'UserDetailsServiceImpl' class for fetching user details from the database using 'username' (with the help of 'UserRepository')

    @Override
    protected void doFilterInternal(        //'doFilterInternal' method (from 'OncePerRequestFilter' class) is overridden to implement the logic for filtering the incoming requests.
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        try
        {
            //Step 1: Extract/get JWT Token From the Authorization Header of the incoming Request
            String jwt = jwtUtils.getJwtFromHeader(request);

            //Step 2: If there is token, Validate the JWT Token
            if(jwt != null && jwtUtils.validateToken(jwt))
            {
                //Step 3: If token is Valid, extract/get username from it and load corresponding User Details from database using UserDetailsService.
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);      //In 'UserDetailsServiceImpl', we override the 'loadUserByUsername' method to load the user details from the database and return the 'User' object as 'UserDetailsImpl' object.

                //Step 4: Set the Authentication in the Security Context Holder (by getting object of 'UsernamePasswordAuthenticationToken' by passing the 'userDetails' and 'null' as credentials)
                if(userDetails != null)
                {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Step 5: Continue with the Filter Chain
        filterChain.doFilter(request, response);


    }
}
