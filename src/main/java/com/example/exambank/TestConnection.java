//package com.example.exambank;
//
//import com.example.exambank.util.HibernateUtil;
//import org.hibernate.Session;
//import org.hibernate.jdbc.Work;
//
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//
//public class TestConnection {
//    public static void main(String[] args) {
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            session.doWork(new Work() {
//                @Override
//                public void execute(Connection connection) throws SQLException {
//                    DatabaseMetaData metaData = connection.getMetaData();
//                    System.out.println(" Kết nối thành công!");
//                    System.out.println(" Server: " + metaData.getURL());
//                    System.out.println(" Tên database: " + connection.getCatalog());
//                    System.out.println(" Người dùng: " + metaData.getUserName());
//                    System.out.println(" Driver: " + metaData.getDriverName());
//                }
//            });
//        } catch (Exception e) {
//            System.err.println(" Lỗi kết nối: " + e.getMessage());
//        } finally {
//            HibernateUtil.shutdown();
//        }
//    }
//}
