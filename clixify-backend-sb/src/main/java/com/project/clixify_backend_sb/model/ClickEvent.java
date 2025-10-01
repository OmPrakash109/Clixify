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
    private LocalDateTime clickDate;

    @ManyToOne      //@ManyToOne as many Click events can be associated with 1 URL Mapping.
    @JoinColumn(name = "uMapId")
    private UrlMapping urlMapping;      //as @ManyToOne so each Click Event will be associated with 1 URL Mapping.

}
