package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;         // câu hỏi với chỗ trống

    @ManyToOne @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Column(name = "suggested_answer", length = 255)
    private String suggestedAnswer; // gợi ý từ AI

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions = new ArrayList<>();

    // Constructors, getters/setters
    public Question() {}
    // … getters & setters …

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getSuggestedAnswer() {
        return suggestedAnswer;
    }

    public void setSuggestedAnswer(String suggestedAnswer) {
        this.suggestedAnswer = suggestedAnswer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<AnswerOption> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOption> options) {
        this.options = options;
    }

    public List<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }
}
