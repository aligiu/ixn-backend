package com.hng.ixn.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")  // to store long text
    private String content;

    @Column(unique = true)
    private Integer nextId;

    @Column(unique = true)
    private Integer prevId;

}
