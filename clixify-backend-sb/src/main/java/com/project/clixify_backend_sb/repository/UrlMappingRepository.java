package com.project.clixify_backend_sb.repository;

import com.project.clixify_backend_sb.model.UrlMapping;
import com.project.clixify_backend_sb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//We are creating a custom repository interface for UrlMapping entity to perform database operations.
@Repository          //Marking the interface with the @Repository annotation to indicate that it is a Spring Managed Repository.
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>   //Extending JpaRepository interface to perform database operations.
{
    UrlMapping findByShortUrl(String shortUrl);     //To fetch the UserMapping object from database with the help of their short URL as there will be scenarios wherein we would want to search a record based on short URL.
    List<UrlMapping> findByUser(User usr);      //To get the all the URL mappings of a particular user from the database.
    //JPA will take care of the rest and will generate the query to fetch the record from the database.
}
