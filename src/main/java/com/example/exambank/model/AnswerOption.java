package com.example.exambank.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "answer_options",
        uniqueConstraints = @UniqueConstraint(columnNames = {"question_id","label"}))
public class AnswerOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "content",columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;      // nội dung lựa chọn

    @Column(length = 1, nullable = false)
    private String label;        // 'A','B','C','D'

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;    // thứ tự hiển thị

    // Constructors, getters/setters
    public AnswerOption() {}
    // … getters & setters …

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}

