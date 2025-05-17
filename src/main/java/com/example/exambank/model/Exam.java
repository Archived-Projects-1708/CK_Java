package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "exam_file_path")
    private String examFilePath;

    @Column(name = "answer_key_path")
    private String answerKeyPath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions = new ArrayList<>();

    // Getters, Setters, Constructors
    public Exam() {}

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }

    // Thêm phương thức để quản lý quan hệ
    public void addQuestion(Question question, int order) {
        ExamQuestion examQuestion = new ExamQuestion(this, question, order);
        examQuestions.add(examQuestion);
        question.getExamQuestions().add(examQuestion);
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExamFilePath() { return examFilePath; }
    public void setExamFilePath(String examFilePath) { this.examFilePath = examFilePath; }
    public String getAnswerKeyPath() { return answerKeyPath; }
    public void setAnswerKeyPath(String answerKeyPath) { this.answerKeyPath = answerKeyPath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<ExamQuestion> getExamQuestions() { return examQuestions; }
}