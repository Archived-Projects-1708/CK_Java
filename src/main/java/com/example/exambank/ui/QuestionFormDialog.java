package com.example.exambank.ui;

import com.example.exambank.model.AnswerOption;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionFormDialog extends JDialog {
    private boolean saved = false;
    private Question question;
    private JTextField txtSuggestedAnswer;
    private List<OptionRowPanel> optionPanels = new ArrayList<>();
    private ButtonGroup optionGroup = new ButtonGroup();

    private JTextArea txtContent;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbLevel;
    private JPanel optionsPanel;
    private JButton btnAddOption;
    private JButton btnSave, btnCancel;

    public QuestionFormDialog(QuestionBankPanel parent, Question question, List<Category> categories, List<Level> levels) {
        super(getOwnerFrame(parent), "Thêm/Sửa Câu hỏi", true);
        this.question = question != null ? question : new Question();

        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(parent);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        txtContent = new JTextArea(this.question.getContent() != null ? this.question.getContent() : "", 3, 50);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);

        cbCategory = new JComboBox<>();
        cbLevel = new JComboBox<>();
        txtSuggestedAnswer = new JTextField(this.question.getSuggestedAnswer() != null ? this.question.getSuggestedAnswer() : "");

        for (Category c : categories) cbCategory.addItem(c.getName());
        for (Level l : levels) cbLevel.addItem(l.getName());

        if (this.question.getCategory() != null) cbCategory.setSelectedItem(this.question.getCategory().getName());
        if (this.question.getLevel() != null) cbLevel.setSelectedItem(this.question.getLevel().getName());

        formPanel.add(new JLabel("Nội dung:"));
        formPanel.add(new JScrollPane(txtContent));
        formPanel.add(new JLabel("Loại:"));
        formPanel.add(cbCategory);
        formPanel.add(new JLabel("Cấp độ:"));
        formPanel.add(cbLevel);
        formPanel.add(new JLabel("Đáp án (nếu tự luận):"));
        formPanel.add(txtSuggestedAnswer);

        add(formPanel, BorderLayout.NORTH);

        // ===== Options Panel =====
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        btnAddOption = new JButton("Thêm lựa chọn");

        btnAddOption.addActionListener(e -> addOptionRow());
        JPanel optHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optHeader.add(new JLabel("Các lựa chọn (nếu trắc nghiệm):"));
        optHeader.add(btnAddOption);
        add(optHeader, BorderLayout.CENTER);
        add(new JScrollPane(optionsPanel), BorderLayout.CENTER);

        // Load existing options if editing
        if (question != null && question.getOptions() != null) {
            for (AnswerOption option : question.getOptions()) {
                addOptionRow(option);
            }
        } else if (question != null) {
            // Đảm bảo options là danh sách rỗng nếu chưa có
            question.setOptions(new ArrayList<>());
        }

        // ===== Buttons =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            this.question.setContent(txtContent.getText().trim());
            String suggestedAnswer = txtSuggestedAnswer.getText().trim();
            this.question.setSuggestedAnswer(txtSuggestedAnswer.getText().trim());

            // Xóa các options cũ và thêm mới từ form
            this.question.getOptions().clear(); // Xóa options cũ
            for (int i = 0; i < optionPanels.size(); i++) {
                OptionRowPanel panel = optionPanels.get(i);
                if (panel.getTxtContent() != null && !panel.getTxtContent().getText().trim().isEmpty()) {
                    AnswerOption option = new AnswerOption();
                    option.setLabel(panel.getTxtLabel().getText()); // Sử dụng label từ giao diện
                    option.setContent(panel.getTxtContent().getText().trim());
                    option.setCorrect(panel.getChkCorrect().isSelected());
                    option.setQuestion(this.question);
                    this.question.getOptions().add(option);
                }
            }

            // Xóa suggestedAnswer nếu có options
            if (!this.question.getOptions().isEmpty()) {
                this.question.setSuggestedAnswer(null);
            }

            // Gán category và level dựa trên combobox
            String selectedCategory = (String) cbCategory.getSelectedItem();
            String selectedLevel = (String) cbLevel.getSelectedItem();
            for (Category c : categories) {
                if (c.getName().equals(selectedCategory)) {
                    this.question.setCategory(c);
                    break;
                }
            }
            for (Level l : levels) {
                if (l.getName().equals(selectedLevel)) {
                    this.question.setLevel(l);
                    break;
                }
            }

            saved = true;
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void addOptionRow() {
        addOptionRow(null);
    }

    private void addOptionRow(AnswerOption existingOption) {
        OptionRowPanel row = new OptionRowPanel();
        if (existingOption != null) {
            row.getTxtLabel().setText(existingOption.getLabel());
            row.getTxtContent().setText(existingOption.getContent());
            row.getChkCorrect().setSelected(existingOption.isCorrect());
        }
        else {
            // Gán label mới cho option mới (ví dụ: tiếp theo chữ cái cuối cùng trong danh sách)
            char newLabel = 'A';
            if (!this.question.getOptions().isEmpty()) {
                char lastLabel = this.question.getOptions().get(this.question.getOptions().size() - 1).getLabel().charAt(0);
                newLabel = (char) (lastLabel + 1);
            }
            row.getTxtLabel().setText(String.valueOf(newLabel));
        }

        row.getBtnRemove().addActionListener(e -> {
            optionsPanel.remove(row);
            optionPanels.remove(row);
            revalidate();
            repaint();
        });

        optionPanels.add(row);
        optionsPanel.add(row);
        revalidate();
        repaint();
    }

    private static Frame getOwnerFrame(Component parent) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        return window instanceof Frame ? (Frame) window : null;
    }

    public boolean isSaved() { return saved; }
    public Question getQuestion() { return question; }
}