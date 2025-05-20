package com.example.exambank.model;

import javax.persistence.*;

@Entity
@Table(name = "exam_questions")
public class ExamQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    // Constructors, getters/setters
    public ExamQuestion() {}
    public ExamQuestion(Exam exam, Question question, int displayOrder) {
        this.exam = exam;
        this.question = question;
        this.displayOrder = displayOrder;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
