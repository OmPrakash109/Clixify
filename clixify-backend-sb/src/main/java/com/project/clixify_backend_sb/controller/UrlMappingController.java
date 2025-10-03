package com.project.clixify_backend_sb.controller;

import com.project.clixify_backend_sb.dtos.ClickEventDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @PostMapping("/shorten")        //It is a URL shortening endpoint
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
    @GetMapping("/myurls")      //It is a URL retrieval endpoint
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal)
    {
        User user = userService.findByUsername(principal.getName());    //First we will get the user from the Security Context. We do so by extracting user's name from principal and using it to find the user from the database with the help of userService findByUsername method.
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);       //Then we will get all the URLs mapped/associated with the user(principal) who made the request, with the help of urlMappingService's getUrlsByUser method, and return List of UrlMappingDTO object in response.
        return ResponseEntity.ok(urls);         //Returning the List of UrlMappingDTO object to the client which is then converted to JSON and sent to the client
    }

    //Controller method with @GetMapping annotation, to handle the GET requests at '/api/urls/analytics/{shortUrl}' endpoint, for getting the analytics of the URL mapped/associated with the user(principal) who made the request, and return List of ClickEventDTO object in response.
    @GetMapping("/analytics/{shortUrl}")    //It is a URL analytics endpoint
    @PreAuthorize("hasRole('USER')")    //shortUrl is passed as part of the URL
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                         @RequestParam("startDate") String startDate,
                                                         @RequestParam("endDate") String endDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;    //Creating a DateTimeFormatter object to parse the date and time. ISO_LOCAL_DATE_TIME is a pre-defined pattern for parsing date and time in the format of 'yyyy-MM-ddTHH:mm:ss' like 2024-01-01T00:00:00, this 2024-12-01T00:00:00 we get from the request parameters @RequestParam "startDate" and "endDate".
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);    //Parsing the start date and time into LocalDateTime object format for the start date and time.
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);        //Parsing the end date and time into LocalDateTime object format for the end date and time.
        List<ClickEventDTO> clickEventDTOS =urlMappingService.getClickEventByDate(shortUrl, start, end);    //Calling the getClickEventByDate method of UrlMappingService to get the analytics of the URL mapped/associated with the user(principal) who made the request, and return List of ClickEventDTO object in response.
        return ResponseEntity.ok(clickEventDTOS);       //Returning the List of ClickEventDTO object to the client which is then converted to JSON and sent to the client
    }



    //Controller method with @GetMapping annotation, to handle the GET requests at '/api/urls/totalClicks' endpoint, for getting the total clicks of the URL mapped/associated with the user(principal) who made the request, and return Map of LocalDate and Long object in response.
    @GetMapping("/totalClicks")     //It is a URL total clicks endpoint
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
                                                                     @RequestParam("startDate") String startDate,
                                                                     @RequestParam("endDate") String endDate)     //We need total Clicks of all the URLs that the user(principal) who made the request has mapped/associated, owns. That's why we need to get the user(principal) who made the request.
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;     //Creating a DateTimeFormatter object to parse the date. ISO_LOCAL_DATE is a pre-defined pattern for parsing date in the format of 'yyyy-MM-dd' like 2024-01-01, this 2024-12-01 we get from the request parameters @RequestParam "startDate" and "endDate".
        User user = userService.findByUsername(principal.getName());        //First we will get the user from the Security Context. We do so by extracting user's name from principal and using it to find the user from the database with the help of userService findByUsername method.
        LocalDate start = LocalDate.parse(startDate, formatter);    //Parsing the start date into LocalDate object format for the start date.
        LocalDate end = LocalDate.parse(endDate, formatter);        //Parsing the end date into LocalDate object format for the end date.
        Map<LocalDate, Long> totalClicks =urlMappingService.getTotalClicksByUserAndDate(user, start, end);      //Calling the getTotalClicksByUserAndDate method of UrlMappingService to get the total clicks of all the URLs that the user(principal) who made the request has mapped/associated, owns, and return Map of LocalDate and Long object in response.
        return ResponseEntity.ok(totalClicks);      //Returning the Map of LocalDate and Long object to the client which is then converted to JSON and sent to the client
    }
}
