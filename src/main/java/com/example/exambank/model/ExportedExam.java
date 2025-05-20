package com.example.exambank.model;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exported_exams")
public class ExportedExam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "paper_path", nullable = false)
    private String paperPath;

    @Column(name = "answer_key_path", nullable = false)
    private String answerKeyPath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors, getters/setters
    public ExportedExam() {}
    public ExportedExam(Exam exam, String paperPath, String answerKeyPath) {
        this.exam = exam;
        this.paperPath = paperPath;
        this.answerKeyPath = answerKeyPath;
    }
    // … getters & setters …

    public Long getId() {
        return id;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getPaperPath() {
        return paperPath;
    }

    public void setPaperPath(String paperPath) {
        this.paperPath = paperPath;
    }

    public String getAnswerKeyPath() {
        return answerKeyPath;
    }

    public void setAnswerKeyPath(String answerKeyPath) {
        this.answerKeyPath = answerKeyPath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}

