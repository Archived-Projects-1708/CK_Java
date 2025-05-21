package com.example.exambank.ui;

import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.service.QuestionImportService;
import com.example.exambank.service.cate_levelService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final JTabbedPane tabbedPane;
    private final QuestionBankPanel questionBankPanel;
    private final ExamPanel       examPanel;

    public MainFrame(QuestionImportService importService) {
        super("Exam Bank Manager (Java 17)");

        // 1) Look & Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2) Panels
        questionBankPanel = new QuestionBankPanel(importService);
        examPanel         = new ExamPanel();

        // 3) Load categories & levels
        cate_levelService catLvlSvc = new cate_levelService();
        List<Category> categories = catLvlSvc.getAllCategories();
        List<Level>    levels     = catLvlSvc.getAllLevels();

        questionBankPanel.loadCategoriesAndLevels(categories, levels);
        examPanel.loadCategoriesAndLevels(categories, levels);

        // 4) Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Ngân hàng câu hỏi", questionBankPanel);
        tabbedPane.addTab("Đề thi", examPanel);

        add(tabbedPane);

        // 5) Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    // Bootstraps the UI
    public static void start(QuestionImportService importService) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(importService);
            frame.setVisible(true);
        });
    }
}
