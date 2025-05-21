package com.example.exambank.dao;

import com.example.exambank.model.AnswerOption;

public class AnswerOptionDao extends AbstractDao<AnswerOption, Long> {
    public AnswerOptionDao() {
        super(AnswerOption.class);
    }
}