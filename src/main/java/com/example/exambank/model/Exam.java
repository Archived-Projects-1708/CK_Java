package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
public class Exam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;   // null nếu tổng hợp

    @ManyToOne @JoinColumn(name = "level_id")
    private Level level;         // null nếu đa cấp độ

    @Column(name = "question_count", nullable = false)
    private int questionCount;   // số câu khi export

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExportedExam> exports = new ArrayList<>();

    // Constructors, getters/setters
    public Exam() {}
    // … getters & setters …

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Level getLevel() {
        return level;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public List<ExportedExam> getExports() {
        return exports;
    }

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void setExports(List<ExportedExam> exports) {
        this.exports = exports;
    }
}
