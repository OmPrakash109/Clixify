package com.project.clixify_backend_sb.controller;

import com.project.clixify_backend_sb.dtos.UrlMappingDTO;
import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.service.UrlMappingService;
import com.project.clixify_backend_sb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")        //Mapping the base URL for all the URL mapping related endpoints
@AllArgsConstructor
public class UrlMappingController
{
    private UrlMappingService urlMappingService;        //We inject the object of UrlMappingService to perform the business logic for URL mapping
    private UserService userService;        //We inject the object of UserService to get the user details from the principal object

    // {"originalUrl":"https://example.com"}  - sample key-value pair passed in @RequestBody where 'originalUrl' is the key and 'https://example.com' is the value - can test on Postman
    // https://abc.com/Hg9K7IJX --> https://example.com         - short URL --> original URL
    // https://xyz.com/WXZfkst5 --> https://spring_boot.com     - short URL --> original URL

    //Controller method with @PostMapping annotation, to handle the POST requests at '/api/urls/shorten' endpoint, for shortening the passed URL, and return UrlMappingDTO object in response.
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")   //This Controller method is an authenticated method so it requires authentication, and we cannot access this endpoint without authentication
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal)  // the Map Stores the key-value pairs from the @RequestBody and Principal stores the user details, and we pass it to the service layer (UrlMappingService) to generate the short URL
    {                                                                                                                   //When the request is authenticated, principle is auto-injected
        String originalUrl = request.get("originalUrl");    //Extracting the original URL from the @RequestBody
        User user = userService.findByUsername(principal.getName());        //Extracting the user details from the principal object as every short URL in the database is mapped to a user and we need to get the user details from the principal object to map the short URL to that user.

        //Calling the createShortUrl method of UrlMappingService to generate the short URL, which interacts with the repository layer(UrlMapping) to save the short URL in the database and returns the 'UrlMappingDTO' object which is then returned to the client
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);      //This method returns the 'UrlMappingDTO' object which has the short URL and original URL and user details
        return ResponseEntity.ok(urlMappingDTO);        //Returning the 'UrlMappingDTO' object to the client which is then converted to JSON and sent to the client
    }

    //Controller method with @GetMapping annotation, to handle the GET requests at '/api/urls/myurls' endpoint, for getting all the URLs mapped/associated with the user(principal) who made the request, and return List of UrlMappingDTO object in response.
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal)
    {
        User user = userService.findByUsername(principal.getName());    //First we will get the user from the Security Context. We do so by extracting user's name from principal and using it to find the user from the database with the help of userService findByUsername method.
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);       //Then we will get all the URLs mapped/associated with the user(principal) who made the request, with the help of urlMappingService's getUrlsByUser method, and return List of UrlMappingDTO object in response.
        return ResponseEntity.ok(urls);         //Returning the List of UrlMappingDTO object to the client which is then converted to JSON and sent to the client
    }
}
