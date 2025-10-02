package com.project.clixify_backend_sb.repository;


import com.project.clixify_backend_sb.model.ClickEvent;
import com.project.clixify_backend_sb.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long>   //Extending JpaRepository interface to perform database operations. And as this Repository interface for ClickEvent entity, it will perform database operations for ClickEvent entity.
{
    //METHOD NAME CONVENTION: findBy + Field Name + Between  : see below for logic
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping mapping, LocalDateTime startDate, LocalDateTime endDate);       // This method will be used to show the total clicks of a specific URL that the specific user(whose urlMapping has been passed) has got and return List of ClickEvent objects in response which will have total clicks of that specific URL.
    //In 'ClickEvent' Entity, we have 'urlMapping' and 'clickDate', so we wrote method name like findByUrlMappingAndClickDateBetween()
    //and we wrote 'Between' as there are two Dates that are being passed as parameters (startDate and endDate) so JPA will convert it into 'BETWEEN' clause and generate the query.
    //And this method will return the list of ClickEvent objects associated with the UrlMapping object.

    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings, LocalDateTime startDate, LocalDateTime endDate);       //This method will be used to show the total clicks of all the URLs that the specific user(whose urlMappings has been passed) has got and return List of ClickEvent objects in response which will have total clicks of all the URLs.

}
