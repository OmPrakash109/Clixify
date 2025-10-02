package com.project.clixify_backend_sb.service;

import com.project.clixify_backend_sb.dtos.UrlMappingDTO;
import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service        //Marking the class with the @Service annotation to indicate that it is a Spring Managed Service.
@AllArgsConstructor     //Marking the class with the @AllArgsConstructor annotation to generate a constructor with all the required fields, which here, we are mainly using for dependency injection of 'UrlMappingRepository'.
public class UrlMappingService
{
    private UrlMappingRepository urlMappingRepository;      //Injecting the 'UrlMappingRepository' dependency into the 'UrlMappingService' class to perform database operations of 'UrlMapping' entity.

    //Business logic for generating short URL
    public UrlMappingDTO createShortUrl(String originalUrl, User user)
    {
        String shortUrl = generateShortUrl();       //Generating the short URL
        UrlMapping urlMapping = new UrlMapping();   //Creating a new UrlMapping object to store the short URL in the database along with the original URL and user details.

        urlMapping.setOriginalUrl(originalUrl);     //Setting the original URL in the UrlMapping object
        urlMapping.setShortUrl(shortUrl);       //Setting the short URL in the UrlMapping object
        urlMapping.setUser(user);           //Setting the user details in the UrlMapping object
        urlMapping.setCreatedDate(LocalDateTime.now());     //Setting the created date in the UrlMapping object

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);    //First Saving the UrlMapping object in the database and returns the saved object and stores it in the 'savedUrlMapping' variable of type 'UrlMapping'.

        return convertToDto(savedUrlMapping);        //Then returning after converting the 'UrlMapping' object to 'UrlMappingDTO' object as 'UrlMappingDTO' is the object that we want to return to the client
    }

    //Business logic for generating short URL(called in createShortUrl method), this is where the actual logic of generating the short URL takes place
    /*
    * Steps for Shortening the Original URL to Short URL:
    * 1. First we will have the length of the short URL set to 8 characters. (We can change this length as per our wish/requirement)
    * 2. Then we take any random character from the 'characters' string (which contains chars [A-Z, a-z, 0-9]) and append it until length of the short URL reaches 8 characters*/
    private String generateShortUrl()
    {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";           //String containing all the characters that can be used to generate the short URL

        Random random = new Random();       //Creating a Random object to generate random numbers
        StringBuilder shortUrl = new StringBuilder(8);      //Creating a StringBuilder object to store the short URL

        //Generating the short URL
        for(int i = 0; i < 8; i++)
        {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));        //appending random char from 'characters' string to 'shortUrl' StringBuilder object
        }

        return shortUrl.toString();     //Returning the generated short URL (after converting type from StringBuilder to String)
    }

    //Business logic for converting UrlMapping object to UrlMappingDTO object(called in createShortUrl method), we convert the UrlMapping object to UrlMappingDTO object to return it to the client as UrlMappingDTO
    private UrlMappingDTO convertToDto(UrlMapping urlMapping)
    {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();      //Instantiating the 'UrlMappingDTO' object to fill it with 'UrlMapping' object details

        urlMappingDTO.setId(urlMapping.getUMapId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());

        return urlMappingDTO;       //returning the 'UrlMappingDTO' object after converting the 'UrlMapping' object to 'UrlMappingDTO' object
    }

    public List<UrlMappingDTO> getUrlsByUser(User user)
    {
        return urlMappingRepository.findByUser(user).stream()   //findByUser(user) method of urlMappindRepository returns a list of UrlMapping objects associated with the user, but we want to return a list of UrlMappingDTO objects associated with the user,
                .map(this::convertToDto)        //so we use stream() to convert it to a stream and then use map() to convert each UrlMapping object to UrlMappingDTO object
                .toList();          //and finally we use toList() to convert the stream to a list and return it
    }
}
