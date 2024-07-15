package com.hng.ixn.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer globalId;

    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)  // to store long text
    private String content;

    @Column
    private Integer nextId;

    @Column
    private Integer prevId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

}
