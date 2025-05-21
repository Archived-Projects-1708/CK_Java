package com.example.exambank.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Tạo registry từ file cấu hình
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

                // Tạo metadata sources
                MetadataSources sources = new MetadataSources(registry);

                // Thêm các entity class
                sources.addAnnotatedClass(com.example.exambank.model.Category.class);
                sources.addAnnotatedClass(com.example.exambank.model.Level.class);
                sources.addAnnotatedClass(com.example.exambank.model.Question.class);
                sources.addAnnotatedClass(com.example.exambank.model.AnswerOption.class);
                sources.addAnnotatedClass(com.example.exambank.model.Exam.class);
                sources.addAnnotatedClass(com.example.exambank.model.ExamQuestion.class);
                sources.addAnnotatedClass(com.example.exambank.model.ExportedExam.class);

                // Tạo metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Tạo SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}