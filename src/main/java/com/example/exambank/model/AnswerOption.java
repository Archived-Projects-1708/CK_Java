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

    @Column(columnDefinition = "TEXT", nullable = false)
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
}

