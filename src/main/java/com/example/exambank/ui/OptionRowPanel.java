package com.example.exambank.ui;

import javax.swing.*;
import java.awt.*;

// 5. OptionRowPanel: một dòng lựa chọn trong QuestionFormDialog
public class OptionRowPanel extends JPanel {
    // checkbox
    private JTextField txtLabel;
    private JTextField txtContent;
    private JCheckBox chkCorrect;

    // radio button
    private JTextField txtOption;
    private JRadioButton radioCorrect;
    // common use
    private JButton btnRemove;
    // Constructor checkbox
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
    // Constructor radiobutton
    public OptionRowPanel(ButtonGroup group) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        txtOption = new JTextField(30);
        radioCorrect = new JRadioButton("Đúng");
        btnRemove = new JButton("Xóa");

        group.add(radioCorrect); // Cho radio này vào ButtonGroup để chọn 1 đáp án đúng

        add(txtOption);
        add(radioCorrect);
        add(btnRemove);
    }

    public String getOptionText() {
        return txtOption != null ? txtOption.getText() : "";
    }

    public boolean isCorrect() {
        return radioCorrect != null && radioCorrect.isSelected();
    }

    public void setOptionText(String text) {
        if (txtOption != null) txtOption.setText(text);
    }

    public void setCorrect(boolean correct) {
        if (radioCorrect != null) radioCorrect.setSelected(correct);
    }
    // Getters …
    public JTextField getTxtLabel()    { return txtLabel; }
    public JTextField getTxtContent()  { return txtContent; }
    public JCheckBox getChkCorrect()   { return chkCorrect; }
    public JButton getBtnRemove()      { return btnRemove; }
}
