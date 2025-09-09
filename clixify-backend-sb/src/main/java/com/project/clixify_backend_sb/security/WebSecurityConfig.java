package com.project.clixify_backend_sb.security;

import com.project.clixify_backend_sb.security.jwt.JwtAuthenticationFilter;
import com.project.clixify_backend_sb.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()
    {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()   //Authentication provider is used to authenticate the user and for that we need to set the user details service and password encoder.
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();   //Create an object of type 'DaoAuthenticationProvider'.
        authProvider.setUserDetailsService(userDetailsService);     //Set the user details service.
        authProvider.setPasswordEncoder(passwordEncoder());         //Set the password encoder.
        return authProvider;                                    //Return the object of type 'DaoAuthenticationProvider'.
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()    //Allow all requests from '/api/auth/**' endpoint.
                        .requestMatchers("/api/url/**").authenticated()     //Allow authenticated requests from '/api/url/**' endpoint.
                        .requestMatchers("/{shortUrl}").permitAll()     //Allow all requests from '/{shortUrl}' endpoint.
                        .anyRequest().authenticated()       //Allow authenticated requests from any other endpoint.
                );

        http.authenticationProvider(authenticationProvider());      //Set the authentication provider before building the filter chain.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);        //We are telling spring to add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter(which is filter for processing forms).
        return http.build();    //Return the object of type 'SecurityFilterChain'.
    }
}
