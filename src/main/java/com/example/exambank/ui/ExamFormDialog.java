package com.example.exambank.ui;

import com.example.exambank.model.Category;
import com.example.exambank.model.Level;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExamFormDialog extends JDialog {
    private boolean saved = false;
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cbCategory, cbLevel;
    private JButton btnSave, btnCancel;

    public ExamFormDialog(Frame owner, List<Category> categories, List<Level> levels) {
        super(owner, "Tạo / Sửa Đề thi", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Tiêu đề:"), gbc);
        txtTitle = new JTextField(30);
        gbc.gridx = 1; add(txtTitle, gbc);

        // Mô tả
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Mô tả:"), gbc);
        txtDescription = new JTextArea(4,30);
        gbc.gridx = 1; add(new JScrollPane(txtDescription), gbc);

        // Loại (Category)
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Loại:"), gbc);
        cbCategory = new JComboBox<>();
        categories.forEach(c -> cbCategory.addItem(c.getName()));
        gbc.gridx = 1; add(cbCategory, gbc);

        // Cấp độ
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Cấp độ:"), gbc);
        cbLevel = new JComboBox<>();
        levels.forEach(l -> cbLevel.addItem(l.getName()));
        gbc.gridx = 1; add(cbLevel, gbc);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(pnlButtons, gbc);

        btnSave.addActionListener(e -> {
            if (txtTitle.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Tiêu đề không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() { return saved; }
    public String getTitleText() { return txtTitle.getText().trim(); }
    public String getDescriptionText() { return txtDescription.getText().trim(); }
    public String getSelectedCategory() { return (String) cbCategory.getSelectedItem(); }
    public String getSelectedLevel() { return (String) cbLevel.getSelectedItem(); }
}
