package com.example.exambank;

import javax.swing.SwingUtilities;
import com.example.exambank.ui.MainFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
