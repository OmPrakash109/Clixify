package com.project.clixify_backend_sb.model;

/*
This is our custom version/representation of 'User' specific to our application's needs.
And with the help of 'UserDetailsImpl' class, we convert our custom representation of user (of type 'User') to Spring Security compatible representation of user (of type 'UserDetailsImpl') so it can work with authentication and authorization of the User.
*/

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity     //@Entity annotation is used to tell spring to persist this as a table in the database/to map this Entity to a Table in the database/to treat this class as a Table in the database
@Table(name = "users")      //@Table annotation is used to set the name of the table in tht database
@Data                   //@Data is a lombok annotation which provides the getters and setters without having to declare it manually
@NoArgsConstructor      //@NoArgsConstructor is a lombok annotation that provides default constructor without having to declare it manually
@AllArgsConstructor     //@AllArgsConstructor is a lombok annotation that provides parameterized constructor without having to declare it manually
public class  User
{
    @Id            //@Id annotation specifies the primary key of the table (to the filed just below it)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //GenerationType.IDENTITY of @GeneratedValue annotation tells spring to Auto-increment primary key id each time new row is added
    private Long userId;
    private String email;
    private String username;
    private String password;
    private String role = "ROLE_USER";

    @OneToMany              //@OneToMany annotation as 1 user can have multiple URL mappings. AND
    @JoinColumn(name = "uMapId")       //@JoinColumn annotation is used to specify the foreign key column name in the current table
    private List<UrlMapping> urlMappings;       //as @OneToMany means 1 user will have List of URL mappings.
}
