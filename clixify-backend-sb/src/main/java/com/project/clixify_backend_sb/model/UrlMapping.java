package com.project.clixify_backend_sb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "UrlMappings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uMapId;
    private String originalUrl;
    private String shortUrl;
    private int clickCount = 0;
    private LocalDateTime createdDate;

    @ManyToOne      //@ManyToOne as many Url mappings can be associated with 1 user. (as from both sides, so bidirectional) AND as we didn't write 'mappedBy' as we want Urlmappping table to have user_id as foreign key column.
    @JoinColumn(name = "user_id")   //@Join Column is used to specify the name of the foreign key column in the database (must match the actual column name)
    private User user;      //as @ManyToOne so only 1 user per UrlMapping

    @OneToMany(mappedBy = "urlMapping")     //@OneToMany as 1 Url Mapping can have multiple Click events. AND as we didn't use 'mappedBy' here so we don't need to use @JoinColumn annotation as when we don't want to have foreign key column then there is no need to specify name for foreign key column.
    private List<ClickEvent> clickEvents;   //as @OneToMany so 1 UrlMapping can be associated with List of ClickEvents.
}

