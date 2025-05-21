package com.example.exambank.service;

import com.example.exambank.dao.CategoryDao;
import com.example.exambank.dao.LevelDao;
import com.example.exambank.dao.QuestionDao;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.model.Question;
import com.example.exambank.ui.QuestionBankPanel;
import com.example.exambank.ui.QuestionFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionBankController {
    private final QuestionBankPanel view;
    private final QuestionDao questionDao;
    private final CategoryDao categoryDao;
    private final LevelDao levelDao;

    public QuestionBankController(QuestionBankPanel view) {
        this.view = view;
        this.questionDao = new QuestionDao();
        this.categoryDao = new CategoryDao();
        this.levelDao = new LevelDao();
        initController();
    }

    private void initController() {
        setupTable();
        setupFilter();
        setupButtons();
    }

    private void setupTable() {
        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nội dung", "Loại", "Cấp độ", "Đáp án"}, 0
        );
        // Sử dụng Set để kiểm tra ID câu hỏi, tránh lặp
        Set<Long> questionIds = new HashSet<>();
        List<Question> questions = questionDao.findAllWithOptions();
        for (Question q : questions) {
            if (!questionIds.contains(q.getId())) {
                String answer;
                if (q.getSuggestedAnswer() != null) {
                    answer = q.getSuggestedAnswer();
                } else {
                    answer = q.getOptions().stream()
                            .filter(opt -> opt.isCorrect())
                            .map(opt -> opt.getLabel() + ": " + opt.getContent())
                            .findFirst()
                            .orElse("Không có đáp án");
                }
                model.addRow(new Object[]{
                        q.getId(),
                        q.getContent(),
                        q.getCategory().getName(),
                        q.getLevel().getName(),
                        answer
                });
                questionIds.add(q.getId());
            }
        }
        view.getTblQuestions().setModel(model);
        view.getTblQuestions().revalidate();
        view.getTblQuestions().repaint();

        // Điều chỉnh kích thước cột
        view.getTblQuestions().getColumnModel().getColumn(0).setPreferredWidth(50);
        view.getTblQuestions().getColumnModel().getColumn(1).setPreferredWidth(300);
        view.getTblQuestions().getColumnModel().getColumn(2).setPreferredWidth(100);
        view.getTblQuestions().getColumnModel().getColumn(3).setPreferredWidth(100);
        view.getTblQuestions().getColumnModel().getColumn(4).setPreferredWidth(200);
    }

    private void setupFilter() {
        for (var listener : view.getBtnFilter().getActionListeners()) {
            view.getBtnFilter().removeActionListener(listener);
        }

        view.getBtnFilter().addActionListener(e -> {
            String categoryName = (String) view.getCbCategory().getSelectedItem();
            String levelName = (String) view.getCbLevel().getSelectedItem();

            Category category = categoryDao.findByName(categoryName).orElse(null);
            Level level = levelDao.findByName(levelName).orElse(null);

            if (category == null || level == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy loại hoặc cấp độ");
                return;
            }
            // Sử dụng Set để kiểm tra ID
            Set<Long> questionIds = new HashSet<>();

            List<Question> filtered = questionDao.findByCategoryAndLevelWithOptions(category, level);

            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"ID", "Nội dung", "Loại", "Cấp độ", "Đáp án"}, 0
            );
            for (Question q : filtered) {
                if (!questionIds.contains(q.getId())) {
                    String answer;
                    if (q.getSuggestedAnswer() != null) {
                        answer = q.getSuggestedAnswer();
                    } else {
                        answer = q.getOptions().stream()
                                .filter(opt -> opt.isCorrect())
                                .map(opt -> opt.getLabel() + ": " + opt.getContent())
                                .findFirst()
                                .orElse("Không có đáp án");
                    }
                    model.addRow(new Object[]{
                            q.getId(),
                            q.getContent(),
                            q.getCategory().getName(),
                            q.getLevel().getName(),
                            answer
                    });
                    questionIds.add(q.getId());
                }
            }
            view.getTblQuestions().setModel(model);
            view.getTblQuestions().revalidate();
            view.getTblQuestions().repaint();
        });

    }

    private void setupButtons() {
        for (var listener : view.getBtnAdd().getActionListeners()) {
            view.getBtnAdd().removeActionListener(listener);
        }
        for (var listener : view.getBtnEdit().getActionListeners()) {
            view.getBtnEdit().removeActionListener(listener);
        }
        for (var listener : view.getBtnDelete().getActionListeners()) {
            view.getBtnDelete().removeActionListener(listener);
        }

        view.getBtnAdd().addActionListener(e -> showQuestionForm(null));
        view.getBtnEdit().addActionListener(e -> editQuestion());
        view.getBtnDelete().addActionListener(e -> deleteQuestion());
    }

    private void showQuestionForm(Question question) {
        QuestionFormDialog form = new QuestionFormDialog(view, question, categoryDao.findAll(), levelDao.findAll());
        form.setVisible(true);

        if (form.isSaved()) {
            Question savedQuestion = form.getQuestion();
            if (savedQuestion.getId() == null) {
                questionDao.save(savedQuestion);
            } else {
                questionDao.update(savedQuestion);
            }
            refreshTable();
        }
    }

    private void editQuestion() {
        int selectedRow = view.getTblQuestions().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn câu hỏi cần sửa");
            return;
        }
        Long id = (Long) view.getTblQuestions().getModel().getValueAt(selectedRow, 0);
        Question question = questionDao.findById(id);
        if (question == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy câu hỏi");
            return;
        }
        showQuestionForm(question);
    }

    private void deleteQuestion() {
        int selectedRow = view.getTblQuestions().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn câu hỏi cần xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn chắc chắn muốn xóa?");
        if (confirm == JOptionPane.YES_OPTION) {
            Long id = (Long) view.getTblQuestions().getModel().getValueAt(selectedRow, 0);
            questionDao.delete(id);
            refreshTable();
        }
    }
}