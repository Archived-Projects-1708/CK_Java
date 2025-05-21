package com.example.exambank.ui;

import com.example.exambank.model.Category;
import com.example.exambank.model.Level;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.*;
import java.awt.*;

// 3. ExamPanel: quản lý CRUD đề thi, filter theo loại/cấp độ, export
public class ExamPanel extends JPanel {
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbLevel;
    private JButton btnFilter;
    private JTable tblExams;
    private JButton btnCreate, btnEdit, btnDelete;
    private JButton btnExport;

    public ExamPanel() {
        // 1) Instantiate
        cbCategory = new JComboBox<>();
        cbLevel    = new JComboBox<>();
        btnFilter  = new JButton("Lọc");
        tblExams   = new JTable();
        btnCreate  = new JButton("Tạo mới");
        btnEdit    = new JButton("Sửa");
        btnDelete  = new JButton("Xóa");
        btnExport  = new JButton("Xuất PDF/DOC");
        setLayout(new BorderLayout(10,10));

        // Top: filter
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Loại:"), gbc);

        gbc.gridx = 1;
        filterPanel.add(cbCategory, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Cấp độ:"), gbc);

        gbc.gridx = 3;
        filterPanel.add(cbLevel, gbc);

        gbc.gridx = 4;
        filterPanel.add(btnFilter, gbc);

        add(new JScrollPane(tblExams), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(btnCreate);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnExport);
        add(actionPanel, BorderLayout.SOUTH);
        add(filterPanel, BorderLayout.NORTH);
    }

    public void loadCategoriesAndLevels(java.util.List<Category> categories,java.util.List<Level> levels) {
        cbCategory.removeAllItems();
        cbLevel.removeAllItems();

        for (Category c : categories) {
            cbCategory.addItem(c.getName());
        }
        for (Level l : levels) {
            cbLevel.addItem(l.getName());
        }
    }

    // Getters for wiring
    public JComboBox<String> getCbCategory() { return cbCategory; }
    public JComboBox<String> getCbLevel()    { return cbLevel; }
    public JButton getBtnFilter()            { return btnFilter; }
    public JTable getTblExams()              { return tblExams; }
    public JButton getBtnCreate()            { return btnCreate; }
    public JButton getBtnEdit()              { return btnEdit; }
    public JButton getBtnDelete()            { return btnDelete; }
    public JButton getBtnExport()            { return btnExport; }
}
