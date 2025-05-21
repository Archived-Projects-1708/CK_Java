package com.example.exambank.dao;

import com.example.exambank.model.ExportedExam;

public class ExportedExamDao extends AbstractDao<ExportedExam, Long> {
    public ExportedExamDao() {
        super(ExportedExam.class);
    }
}