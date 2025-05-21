package com.example.exambank.dao;

import com.example.exambank.model.Exam;

public class ExamDao extends AbstractDao<Exam, Long> {
    public ExamDao() {
        super(Exam.class);
    }
}