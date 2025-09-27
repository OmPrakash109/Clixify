package com.project.clixify_backend_sb.security;

import com.project.clixify_backend_sb.security.jwt.JwtAuthenticationFilter;
import com.project.clixify_backend_sb.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration          //Marks the class as a configuration class.
@EnableWebSecurity      //Enables Spring Security's web security features.
@EnableMethodSecurity   //Enables security features at method level.
@AllArgsConstructor
public class WebSecurityConfig
{
    private UserDetailsServiceImpl userDetailsService;

    //Method to create a @Bean of type 'JwtAuthenticationFilter' which returns the object of type 'JwtAuthenticationFilter'.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()
    {
        return new JwtAuthenticationFilter();
    }

    //Method to create a @Bean of type 'PasswordEncoder' which returns the object of type 'BCryptPasswordEncoder'. It is used to encode the password using bcrypt password hashing function.
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();     //Bcrypt is a secure password hashing function designed to protect passwords by making them computationally expensive to crack. It is based on the Blowfish cipher and incorporates features like salting and adaptive hashing to enhance security. However, it is important to note that bcrypt is a one-way hashing algorithm, meaning it is designed to be irreversible. This makes it impossible to "decode" a bcrypt hash back into the original password.
    }

    //Method to create a @Bean of type 'DaoAuthenticationProvider' which returns the object of type 'DaoAuthenticationProvider'.
    //This DaoAuthenticationProvider is used by 'AuthenticationManager' to authenticate the user, which is the under the hood logic of authenticating the user in service/UserService class.
    @Bean
    public DaoAuthenticationProvider authenticationProvider()   //Authentication provider is used to authenticate the user and for that we need to set the user details service and password encoder.
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();   //Create an object of type 'DaoAuthenticationProvider'.
        authProvider.setUserDetailsService(userDetailsService);     //Set the user details service.
        authProvider.setPasswordEncoder(passwordEncoder());         //Set the password encoder.
        return authProvider;                                    //Return the object of type 'DaoAuthenticationProvider'.
    }

    @Bean       //The @Bean annotation is essential for Spring to recognize and use your security configuration. Without it, Spring Security will use its default configuration, which includes CSRF protection.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()    //Allow all requests from '/api/auth/**' endpoint.
                        .requestMatchers("/api/url/**").authenticated()     //Allow authenticated requests from '/api/url/**' endpoint.
                        .requestMatchers("/{shortUrl}").permitAll()     //Allow all requests from '/{shortUrl}' endpoint.
                        .anyRequest().authenticated()       //Allow authenticated requests from any other endpoint.
                );

        http.authenticationProvider(authenticationProvider());      //Set the authentication provider before adding jwtAuthenticationFilter to the filter chain.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);        //We are telling spring to add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter(which is filter for processing forms).
        return http.build();    //Return the object of type 'SecurityFilterChain'.
    }

    //Method to create a @Bean of type 'AuthenticationManager' which returns the object of type 'AuthenticationManager'.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception        //AuthenticationManager is used to authenticate the user and for that we need to set the authentication provider.
    {                                                                                                                                   //don't forget to throw exception as it is a checked exception.
        return authenticationConfiguration.getAuthenticationManager();
    }
}


/*
Understanding Bcrypt and Why It Cannot Be Decoded ---
Bcrypt is a secure password hashing function designed to protect passwords by making them computationally expensive to crack. It is based on the Blowfish cipher and incorporates features like salting and adaptive hashing to enhance security. However, it is important to note that bcrypt is a one-way hashing algorithm, meaning it is designed to be irreversible. This makes it impossible to "decode" a bcrypt hash back into the original password.

Key Features of Bcrypt:-

Salting: Bcrypt generates a unique random salt for each password, ensuring that even identical passwords produce different hashes. This protects against rainbow table attacks.
Adaptive Hashing: The cost factor (number of salt rounds) can be adjusted to increase the computational effort required to hash a password, making it more resistant to brute-force attacks as hardware improves.
Fixed-Length Output: Regardless of the input, bcrypt always produces a 60-character hash.
*/