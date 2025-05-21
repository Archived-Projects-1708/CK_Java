package com.example.exambank.service;

import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class cate_levelService {
    public List<Category> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Category", Category.class).list();
        }
    }

    public List<Level> getAllLevels() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Level", Level.class).list();
        }
    }
}
