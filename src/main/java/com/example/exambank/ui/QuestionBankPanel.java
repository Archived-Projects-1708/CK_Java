package com.example.exambank.ui;

import javax.swing.*;
import java.awt.*;

// 2. QuestionBankPanel: quản lý CRUD câu hỏi, filter theo loại và cấp độ, + import ảnh
public class QuestionBankPanel extends JPanel {
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbLevel;
    private JButton btnFilter;
    private JTable tblQuestions;
    private JButton btnAdd, btnEdit, btnDelete;
    private JButton btnImportImage;

    public QuestionBankPanel() {
        setLayout(new BorderLayout(10,10));

        // Top: filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbCategory = new JComboBox<>();
        cbLevel = new JComboBox<>();
        btnFilter = new JButton("Lọc");
        filterPanel.add(new JLabel("Loại:")); filterPanel.add(cbCategory);
        filterPanel.add(new JLabel("Cấp độ:")); filterPanel.add(cbLevel);
        filterPanel.add(btnFilter);
        add(filterPanel, BorderLayout.NORTH);

        // Center: table
        tblQuestions = new JTable();
        add(new JScrollPane(tblQuestions), BorderLayout.CENTER);

        // Bottom: action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnImportImage = new JButton("Nhập từ ảnh");
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        actionPanel.add(btnImportImage);
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);
    }

    // Getters for controller wiring
    public JComboBox<String> getCbCategory() { return cbCategory; }
    public JComboBox<String> getCbLevel()    { return cbLevel; }
    public JButton getBtnFilter()            { return btnFilter; }
    public JTable getTblQuestions()          { return tblQuestions; }
    public JButton getBtnAdd()               { return btnAdd; }
    public JButton getBtnEdit()              { return btnEdit; }
    public JButton getBtnDelete()            { return btnDelete; }
    public JButton getBtnImportImage()       { return btnImportImage; }
}
