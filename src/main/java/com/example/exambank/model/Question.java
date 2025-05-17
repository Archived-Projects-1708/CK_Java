package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String answer;

    @Column(name = "ai_answer_hint", columnDefinition = "NVARCHAR(MAX)")
    private String aiAnswerHint;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions = new ArrayList<>();

    // Constructors
    public Question() {}

    public Question(String content, String answer, String aiAnswerHint) {
        this.content = content;
        this.answer = answer;
        this.aiAnswerHint = aiAnswerHint;
    }
    public List<ExamQuestion> getExamQuestions() { return examQuestions; }
    public void setExamQuestions(List<ExamQuestion> examQuestions) { this.examQuestions = examQuestions; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getAiAnswerHint() { return aiAnswerHint; }
    public void setAiAnswerHint(String aiAnswerHint) { this.aiAnswerHint = aiAnswerHint; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

