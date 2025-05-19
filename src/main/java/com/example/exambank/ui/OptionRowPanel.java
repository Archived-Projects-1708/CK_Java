package com.example.exambank.ui;

import javax.swing.*;
import java.awt.*;

// 5. OptionRowPanel: một dòng lựa chọn trong QuestionFormDialog
public class OptionRowPanel extends JPanel {
    private JTextField txtLabel;
    private JTextField txtContent;
    private JCheckBox chkCorrect;
    private JButton btnRemove;

    public OptionRowPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        txtLabel = new JTextField(2);
        txtContent = new JTextField(30);
        chkCorrect = new JCheckBox("Đúng");
        btnRemove = new JButton("X");
        add(new JLabel("Nhãn:")); add(txtLabel);
        add(new JLabel("Nội dung:")); add(txtContent);
        add(chkCorrect);
        add(btnRemove);
    }

    // Getters …
    public JTextField getTxtLabel()    { return txtLabel; }
    public JTextField getTxtContent()  { return txtContent; }
    public JCheckBox getChkCorrect()   { return chkCorrect; }
    public JButton getBtnRemove()      { return btnRemove; }
}
