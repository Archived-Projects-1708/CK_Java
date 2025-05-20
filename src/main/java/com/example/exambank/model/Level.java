package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "levels")
public class Level {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String name;         // N5, N4, …

    @Column(columnDefinition = "TEXT")
    private String description;

    // Constructors, getters/setters
    public Level() {}
    public Level(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // … getters & setters …

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
