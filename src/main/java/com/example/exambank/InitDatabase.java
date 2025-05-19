//package com.example.exambank;
//
//import com.example.exambank.util.HibernateUtil;
//
//public class InitDatabase {
//    public static void main(String[] args) {
//        var session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();           // ← important
//        session.getTransaction().commit();    // ← this is where Hibernate runs the CREATE TABLEs
//        session.close();
//        HibernateUtil.shutdown();
//        System.out.println("Schema generation complete. Check your DB!");
//    }
//}
