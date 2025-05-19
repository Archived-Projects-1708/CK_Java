package com.example.exambank.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Load Hibernate configuration
            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")

                    // 2. Register every @Entity so Hibernate knows to map them
                    .addAnnotatedClass(com.example.exambank.model.Category.class)
                    .addAnnotatedClass(com.example.exambank.model.Level.class)
                    .addAnnotatedClass(com.example.exambank.model.Question.class)
                    .addAnnotatedClass(com.example.exambank.model.AnswerOption.class)
                    .addAnnotatedClass(com.example.exambank.model.Exam.class)
                    .addAnnotatedClass(com.example.exambank.model.ExamQuestion.class)
                    .addAnnotatedClass(com.example.exambank.model.ExportedExam.class);


            // Load config.properties
            Properties props = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                props.load(input);
            }

            // Apply properties to Hibernate configuration
            configuration.setProperties(props);
            // configuration.configure("hibernate.cfg.xml"); // Load XML mappings

            // Build SessionFactory
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, "Lỗi khởi tạo Hibernate", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
