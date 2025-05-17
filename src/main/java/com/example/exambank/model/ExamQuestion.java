package com.example.exambank.model;

import javax.persistence.*;

@Entity
@Table(name = "exam_questions")
public class ExamQuestion {
    @EmbeddedId
    private ExamQuestionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    // Getters, Setters, Constructors
    public ExamQuestion() {}

    public ExamQuestion(Exam exam, Question question, int order) {
        this.id = new ExamQuestionId(exam.getId(), question.getId());
        this.exam = exam;
        this.question = question;
        this.questionOrder = order;
    }
    public ExamQuestionId getId() { return id; }
    public void setId(ExamQuestionId id) { this.id = id; }
    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public Integer getOrder() { return questionOrder; }
    public void setOrder(Integer orderNo) { this.questionOrder = orderNo; }
}