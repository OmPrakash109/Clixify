package com.project.clixify_backend_sb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "urlMappings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class URLMapping
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uMap_id;
    private String originalURL;
    private String shortURL;
    private int clickCount = 0;
    private LocalDateTime createdDate;

    @ManyToOne      //@ManyToOne as many URL mappings can be associated with 1 user. (as from both sides, so bidirectional) AND as we didn't write 'mappedBy' as we want urlmappping table to have user_id as foreign key column.
    @JoinColumn(name = "user_id")   //@Join Column is used to specify the name of the foreign key column.
    private User user;      //as @ManyToOne so only 1 user per URLMapping

    @OneToMany(mappedBy = "urlMapping")     //@OneToMany as 1 URL Mapping can have multiple Click events. AND as we didn't use 'mappedBy' here so we don't need to use @JoinColumn annotation as when we don't want to have foreign key column then there is no need to specify name for foreign key column.
    private List<ClickEvent> clickEvents;   //as @OneToMany so 1 URLMapping can be associated with List of ClickEvents.
}
