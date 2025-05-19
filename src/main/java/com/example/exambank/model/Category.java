package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;         // Kanji, Vocabulary, Grammar

    @Column(columnDefinition = "TEXT")
    private String description;

    // Constructors, getters/setters
    public Category() {}
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // … getters & setters …
}
