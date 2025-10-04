package com.project.clixify_backend_sb.controller;

import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController     //This is a controller class for handling the redirect functionality.
@AllArgsConstructor     //IMPORTANT - This is a Lombok annotation to inject the dependencies automatically, So here it will inject the 'UrlMappingService' dependency OR ELSE it will throw NullPointerException.
public class RedirectController
{
    private UrlMappingService urlMappingService;    //Dependency injection for UrlMappingService as it has business logic method for getting the original URL from the short URL.

    //Controller method with @GetMapping annotation, to handle the GET requests at '/{shortUrl}' endpoint, for redirecting the user to the original URL mapped to the short URL.
    @GetMapping("/{shortUrl}")      //shortUrl is passed as part of the URL directly like http://localhost:8080/{shortUrl} as we have defined the path variable in the @GetMapping annotation (that's why we have not used @RequestMapping annotation) alongside @RestController annotation.
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl)
    {
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl); //Getting the original URL from the short URL using the business logic method of UrlMappingService, which returns a UrlMapping object, and we can extract the original URL from it.
        if(urlMapping != null)          //If the urlMapping Object is not null, then we need to redirect the user to the original URL mapped to the short URL.
        {

            HttpHeaders httpHeaders = new HttpHeaders();    //First creating a new HttpHeaders object to store the headers in the response as we are making use of HttpHeader for redirect functionality.
            httpHeaders.add("Location", urlMapping.getOriginalUrl());   //Then adding the original URL to the HttpHeaders object as a header, with key as "Location" and value as the original URL.
            return ResponseEntity.status(302).headers(httpHeaders).build();     //Then returning the ResponseEntity object with status code 302, along with the HttpHeaders object containing the original URL, as response.
        }
        else        //If the urlMapping Object is null, then we need to return a not found response.
        {
            return ResponseEntity.notFound().build();
        }

    }
}
