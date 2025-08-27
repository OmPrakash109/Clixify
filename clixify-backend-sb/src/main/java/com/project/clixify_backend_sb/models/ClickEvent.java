package com.project.clixify_backend_sb.models;

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
    private Long clickEvent_id;
    private LocalDateTime clickDate;

    @ManyToOne      //@ManyToOne as many Click events can be associated with 1 URL Mapping.
    @JoinColumn(name = "uMap_id")
    private URLMapping urlMapping;      //as @ManyToOne so each Click Event will be associated with 1 URL Mapping.

}
