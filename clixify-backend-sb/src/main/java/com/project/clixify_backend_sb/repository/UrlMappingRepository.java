package com.project.clixify_backend_sb.repository;

import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//We are creating a custom repository interface for UrlMapping entity to perform database operations.
@Repository          //Marking the interface with the @Repository annotation to indicate that it is a Spring Managed Repository.
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>   //Extending JpaRepository interface to perform database operations. And as this Repository interface for UrlMapping entity, it will perform database operations for UrlMapping entity.
{
    //METHOD NAME CONVENTION: findBy + Field Name
    UrlMapping findByShortUrl(String shortUrl);     //To fetch the UrlMapping object from database with the help of their short URL, this UrlMapping object contains the original URL as well, so we call this method (through urlMappingRepository object which was injected) in UrlMappingService's getOriginalUrl(String shortUrl) method,
                                                    // which inturn is called in RedirectController class for fetching the Original URL mapped to passed short URL, and this findByShortUrl(String shortUrl) method returns an object of 'UrlMapping', from which we extract the original URL in RestController's redirect() method and then pass the original in HttpHeaders and thus user gets redirected to Original URL, when it hits the short URL.
    List<UrlMapping> findByUser(User usr);      //To get the all the URL mappings of a particular user from the database.
    //JPA will take care of the rest and will generate the query to fetch the record from the database.
}
