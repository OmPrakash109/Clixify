package com.project.clixify_backend_sb.service;

import com.project.clixify_backend_sb.dtos.ClickEventDTO;
import com.project.clixify_backend_sb.dtos.UrlMappingDTO;
import com.project.clixify_backend_sb.model.ClickEvent;
import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.model.User;
import com.project.clixify_backend_sb.repository.ClickEventRepository;
import com.project.clixify_backend_sb.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service        //Marking the class with the @Service annotation to indicate that it is a Spring Managed Service.
@AllArgsConstructor     //Marking the class with the @AllArgsConstructor annotation to generate a constructor with all the required fields, which here, we are mainly using for dependency injection of 'UrlMappingRepository'.
public class UrlMappingService
{
    private UrlMappingRepository urlMappingRepository;      //Injecting the 'UrlMappingRepository' dependency into the 'UrlMappingService' class to perform database operations of 'UrlMapping' entity.
    private ClickEventRepository clickEventRepository;      //Injecting the 'ClickEventRepository' dependency into the 'UrlMappingService' class to perform database operations of 'ClickEvent' entity.

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

    //Business logic for getting the list of UrlMappingDTO objects associated with the user(called in getUrlsByUser method)
    public List<UrlMappingDTO> getUrlsByUser(User user)
    {
        return urlMappingRepository.findByUser(user).stream()   //findByUser(user) method of urlMappindRepository returns a list of UrlMapping objects associated with the user, but we want to return a list of UrlMappingDTO objects associated with the user,
                .map(this::convertToDto)        //so we use stream() to convert it to a stream and then use map() to convert each UrlMapping object to UrlMappingDTO object
                .toList();          //and finally we use toList() to convert the stream to a list and return it
    }

    //Business logic for getting the analytics of the URL mapped/associated with the user(principal) who made the request(called in getUrlAnalytics method), and return List of ClickEventDTO object in response.
    //In this method we write the business logic wherein we'll be involving Repository and with the help of repository, we are going to get the list of ClickEvent objects, and then we'll be converting it to list of ClickEventDTO objects and return it to the controller.
    public List<ClickEventDTO> getClickEventByDate(String shortUrl, LocalDateTime start, LocalDateTime end)
    {
        //First we need UrlMapping object associated with the shortUrl to get the list of ClickEvent objects associated with the UrlMapping object
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if(urlMapping != null)
        {
            //If UrlMapping object is not null, then we need to get the list of ClickEvent objects associated with the UrlMapping object
            //And then we need to group the ClickEvent objects by date and count the number of clicks on each date
            //And finally we need to convert the list of ClickEvent objects to list of ClickEventDTO objects
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()           //findByUrlMappingAndClickDateBetween method of clickEventRepository returns a list of ClickEvent objects associated with the UrlMapping object
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))         //collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting())) groups the ClickEvent objects by date and counts the number of clicks on each date
                    .entrySet().stream()                        //entrySet() returns a set of all the entries in the map
                    .map(entry -> {             //map() is used to convert each entry in the map to a ClickEventDTO object
                       ClickEventDTO clickEventDTO = new ClickEventDTO();               //Instantiating the 'ClickEventDTO' object to fill it with 'ClickEvent' object details
                       clickEventDTO.setClickDate(entry.getKey());                  //setClickDate() method of ClickEventDTO object is used to set the clickDate of the ClickEventDTO object
                       clickEventDTO.setCount(entry.getValue());                //setCount() method of ClickEventDTO object is used to set the count of the ClickEventDTO object
                       return clickEventDTO;                        //returning the 'ClickEventDTO' object after converting the 'ClickEvent' object to 'ClickEventDTO' object
                    })
                    .collect(Collectors.toList());              //collect(Collectors.toList()) collects the ClickEventDTO objects into a list
        }
        return null;        //If no UrlMapping object is found associated with the shortUrl, then return null
    }

    //Business logic for getting the total clicks by user and date(called in getTotalClicksByUserAndDate method)
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end)
    {
        //First we need to get the list of UrlMapping objects associated with the user
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);

        //Then we need to get the list of ClickEvent objects associated with the UrlMapping objects
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());   //findByUrlMappingInAndClickDateBetween method of clickEventRepository returns a list of ClickEvent objects associated with the UrlMapping objects

        //Then we Collect the data and, we need to group the ClickEvent objects by date and count the number of clicks on each date and return it as a map
        return clickEvents.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
    }
}
