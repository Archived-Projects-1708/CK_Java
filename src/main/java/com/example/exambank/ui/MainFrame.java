package com.example.exambank.ui;

import javax.swing.*;
import java.awt.*;

// 1. MainFrame: Cửa sổ chính, với 2 tab: "Ngân hàng đề" và "Đề thi"
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private QuestionBankPanel questionBankPanel;
    private ExamPanel examPanel;

    public MainFrame() {
        super("Exam Bank Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        questionBankPanel = new QuestionBankPanel();
        examPanel = new ExamPanel();

        tabbedPane.addTab("Ngân hàng câu hỏi", questionBankPanel);
        tabbedPane.addTab("Đề thi", examPanel);

        add(tabbedPane);
    }

    public QuestionBankPanel getQuestionBankPanel() {
        return questionBankPanel;
    }
    public ExamPanel getExamPanel() {
        return examPanel;
    }
}
