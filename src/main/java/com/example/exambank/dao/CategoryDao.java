package com.example.exambank.dao;

import com.example.exambank.model.Category;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Optional;

public class CategoryDao extends AbstractDao<Category, Long> {
    public CategoryDao() {
        super(Category.class);
    }

    public Optional<Category> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Category category = session.createQuery(
                            "FROM Category WHERE name = :name", Category.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(category);
        }
    }
}