package com.project.clixify_backend_sb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clickEvents")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClickEvent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clickEventId;
    private LocalDateTime clickDate;        //clickDate is the date and time when the click event occurred and we are storing both date and time, so we use LocalDateTime. Else we can use LocalDate to store only date.
                                            //We are storing time along with date as if we want to filter the click events by date as well as by time, so it's better to have granular level information.
    @ManyToOne      //@ManyToOne as many Click events can be associated with 1 URL Mapping.
    @JoinColumn(name = "uMapId")
    private UrlMapping urlMapping;      //as @ManyToOne so each Click Event will be associated with 1 URL Mapping.

}

/*
With this Entity/table:
Each Click events get recorded against each mapped URL for analytics purpose (click count by date), for each user.
*/
