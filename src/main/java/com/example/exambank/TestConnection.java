//package com.example.exambank;
//
//import com.example.exambank.util.HibernateUtil;
//
//public class TestConnection {
//    public static void main(String[] args) {
//        try {
//            HibernateUtil.getSessionFactory().openSession();
//            System.out.println("Kết nối thành công đến SQL Server!");
//            HibernateUtil.shutdown();
//        } catch (Exception e) {
//            System.err.println("Lỗi kết nối: " + e.getMessage());
//        }
//    }
//}
