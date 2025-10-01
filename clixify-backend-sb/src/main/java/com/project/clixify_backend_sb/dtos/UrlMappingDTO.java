package com.project.clixify_backend_sb.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UrlMappingDTO          //DTO class for URL mapping - we use UrlMappingDTP instead of UrlMapping Entity as we don't want to expose all the fields of the Entity to the client.
{
    private Long id;        //Unique identifier for the URL mapping
    private String originalUrl;     //Original URL
    private String shortUrl;        //Short URL
    private int clickCount;         //Number of clicks on the short URL
    private LocalDateTime createdDate;  //Date when the short URL was created
    private String username;        //Username of the user who created the short URL
}
