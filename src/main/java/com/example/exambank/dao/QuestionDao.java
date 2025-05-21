package com.example.exambank.dao;

import com.example.exambank.model.Question;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class QuestionDao extends AbstractDao<Question, Long> {
    public QuestionDao() {
        super(Question.class);
    }

    public List<Question> findByCategory(Category category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Question WHERE category = :category", Question.class)
                    .setParameter("category", category)
                    .list();
        }
    }

    public List<Question> findByLevel(Level level) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Question WHERE level = :level", Question.class)
                    .setParameter("level", level)
                    .list();
        }
    }

    public List<Question> findByCategoryAndLevel(Category category, Level level) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Question WHERE category = :category AND level = :level", Question.class)
                    .setParameter("category", category)
                    .setParameter("level", level)
                    .list();
        }
    }
}