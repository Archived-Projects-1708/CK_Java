package com.example.exambank.ui;

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
        setLayout(new BorderLayout(10,10));

        // Top: filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbCategory = new JComboBox<>();
        cbLevel = new JComboBox<>();
        btnFilter = new JButton("Lọc");
        filterPanel.add(new JLabel("Loại:")); filterPanel.add(cbCategory);
        filterPanel.add(new JLabel("Cấp độ:")); filterPanel.add(cbLevel);
        filterPanel.add(btnFilter);
        add(filterPanel, BorderLayout.NORTH);

        // Center: table
        tblExams = new JTable();
        add(new JScrollPane(tblExams), BorderLayout.CENTER);

        // Bottom: actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCreate = new JButton("Tạo mới");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnExport = new JButton("Xuất PDF/DOC");
        actionPanel.add(btnCreate);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnExport);
        add(actionPanel, BorderLayout.SOUTH);
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
