package com.example.exambank.ui;

import javax.swing.*;
import java.awt.*;

// 4. QuestionFormDialog: dialog để thêm/sửa câu hỏi + các option
public class QuestionFormDialog extends JDialog {
    private JTextArea txtContent;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbLevel;
    private JPanel optionsPanel;  // chứa nhiều OptionRowPanel
    private JButton btnAddOption;
    private JButton btnSave, btnCancel;

    public QuestionFormDialog(Frame owner) {
        super(owner, "Thêm/Sửa Câu hỏi", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));

        // Content + metadata
        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        txtContent = new JTextArea(3, 50);
        topPanel.add(new JLabel("Nội dung câu hỏi:"), BorderLayout.NORTH);
        topPanel.add(new JScrollPane(txtContent), BorderLayout.CENTER);
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbCategory = new JComboBox<>();
        cbLevel = new JComboBox<>();
        metaPanel.add(new JLabel("Loại:")); metaPanel.add(cbCategory);
        metaPanel.add(new JLabel("Cấp độ:")); metaPanel.add(cbLevel);
        topPanel.add(metaPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Options list
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        btnAddOption = new JButton("Thêm lựa chọn");
        JPanel optHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optHeader.add(new JLabel("Các lựa chọn:"));
        optHeader.add(btnAddOption);
        add(optHeader, BorderLayout.CENTER);
        add(new JScrollPane(optionsPanel), BorderLayout.CENTER);

        // Save/Cancel
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        bottom.add(btnSave);
        bottom.add(btnCancel);
        add(bottom, BorderLayout.SOUTH);
    }

    // Getters …
    public JTextArea getTxtContent()    { return txtContent; }
    public JComboBox<String> getCbCategory() { return cbCategory; }
    public JComboBox<String> getCbLevel()    { return cbLevel; }
    public JPanel getOptionsPanel()     { return optionsPanel; }
    public JButton getBtnAddOption()    { return btnAddOption; }
    public JButton getBtnSave()         { return btnSave; }
    public JButton getBtnCancel()       { return btnCancel; }
}
