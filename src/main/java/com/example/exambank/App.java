//package com.example.exambank;
//
//import javax.swing.SwingUtilities;
//import com.example.exambank.ui.MainFrame;
//import com.example.exambank.util.HibernateUtil;
//
//public class App {
//    public static void main(String[] args) {
//        HibernateUtil.getSessionFactory().openSession().close();
//
//        SwingUtilities.invokeLater(() -> {
//            MainFrame frame = new MainFrame();
//            frame.setVisible(true);
//        });
//    }
//}
