package com.example.exambank.dao;

import com.example.exambank.model.Question;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

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
    // Phương thức mới để lấy tất cả Question kèm options
    public List<Question> findAllWithOptions() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q FROM Question q LEFT JOIN FETCH q.options", Question.class)
                    .list();
        }
    }

    // Phương thức mới để lọc Question theo category và level kèm options
    public List<Question> findByCategoryAndLevelWithOptions(Category category, Level level) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT q FROM Question q LEFT JOIN FETCH q.options WHERE q.category = :category AND q.level = :level",
                            Question.class
                    )
                    .setParameter("category", category)
                    .setParameter("level", level)
                    .list();
        }
    }

    // Phương thức tìm theo name (nếu cần)
    public Optional<Question> findByName(String name) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT q FROM Question q WHERE q.content = :name", Question.class)
                    .setParameter("name", name)
                    .getResultList()
                    .stream()
                    .findFirst();
        }
    }
    @Override
    public Question findById(Long id) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT q FROM Question q LEFT JOIN FETCH q.options WHERE q.id = :id", Question.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}