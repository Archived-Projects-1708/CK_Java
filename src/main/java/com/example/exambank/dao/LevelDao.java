package com.example.exambank.dao;

import com.example.exambank.model.Level;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Optional;

public class LevelDao extends AbstractDao<Level, Long> {
    public LevelDao() {
        super(Level.class);
    }

    public Optional<Level> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Level level = session.createQuery(
                            "FROM Level WHERE name = :name", Level.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(level);
        }
    }
}