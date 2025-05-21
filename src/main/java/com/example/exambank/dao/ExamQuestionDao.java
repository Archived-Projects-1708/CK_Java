package com.example.exambank.dao;

import com.example.exambank.model.ExamQuestion;

public class ExamQuestionDao extends AbstractDao<ExamQuestion, Long> {
    public ExamQuestionDao() {
        super(ExamQuestion.class);
    }
}