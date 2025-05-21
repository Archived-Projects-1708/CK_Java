package com.example.exambank.dao;

import com.example.exambank.model.Exam;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ExamDao extends AbstractDao<Exam, Long> {
    public ExamDao() {
        super(Exam.class);
    }

    public List<Exam> findAllWithCategoryAndLevel() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT e FROM Exam e " +
                                    "LEFT JOIN FETCH e.category " +
                                    "LEFT JOIN FETCH e.level", Exam.class)
                    .getResultList();
        }
    }
}