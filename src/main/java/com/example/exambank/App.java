package com.example.exambank;

import com.example.exambank.service.QuestionImportService;
import com.example.exambank.ui.MainFrame;
import com.example.exambank.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class App {
    public static void main(String[] args) {
        System.out.println("Khởi động ứng dụng...");

        try {
            // Khởi tạo SessionFactory từ HibernateUtil
            System.out.println("Đang khởi tạo SessionFactory...");
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            // Mở session để kiểm tra kết nối database
            try (Session session = sessionFactory.openSession()) {
                System.out.println("Mở session thành công.");

                // Kiểm tra thông tin kết nối database
                session.doWork(connection -> {
                    DatabaseMetaData metaData = connection.getMetaData();
                    System.out.println("Kết nối thành công!");
                    System.out.println("URL: " + metaData.getURL());
                    System.out.println("🗃Database: " + connection.getCatalog());
                    System.out.println("User: " + metaData.getUserName());
                    System.out.println("Driver: " + metaData.getDriverName());
                });

                // Tạo EntityManager từ SessionFactory
                EntityManager entityManager = sessionFactory.createEntityManager();

                // Khởi tạo QuestionImportService với EntityManager
                QuestionImportService importService = new QuestionImportService(entityManager);

                // Khởi tạo và hiển thị MainFrame trên Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(() -> {
                    MainFrame mainFrame = new MainFrame(importService);
                    mainFrame.setVisible(true);
                });

            } catch (Exception e) {
                System.err.println("Lỗi khi mở session hoặc thực thi kiểm tra kết nối:");
                e.printStackTrace();
            }

        } catch (Throwable ex) {
            System.err.println("Lỗi khi khởi tạo Hibernate SessionFactory:");
            ex.printStackTrace();
        } finally {
            // Thêm shutdown hook để giải phóng tài nguyên khi ứng dụng đóng
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Đang tắt Hibernate...");
                HibernateUtil.shutdown();
            }));
        }
    }
}