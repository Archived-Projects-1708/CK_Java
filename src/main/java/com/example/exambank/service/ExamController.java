package com.example.exambank.service;

import com.example.exambank.dao.ExamDao;
import com.example.exambank.dao.QuestionDao;
import com.example.exambank.dao.CategoryDao;
import com.example.exambank.dao.LevelDao;
import com.example.exambank.model.Exam;
import com.example.exambank.model.Question;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;
import com.example.exambank.model.ExamQuestion;
import com.example.exambank.ui.ExamFormDialog;
import com.example.exambank.ui.ExamPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExamController {
    private final ExamPanel view;
    private final ExamDao examDao;
    private final QuestionDao questionDao;
    private final CategoryDao categoryDao;
    private final LevelDao levelDao;

    public ExamController(ExamPanel view) {
        this.view = view;
        this.examDao = new ExamDao();
        this.questionDao = new QuestionDao();
        this.categoryDao = new CategoryDao();
        this.levelDao = new LevelDao();
        initController();
    }

    private void initController() {
        setupTable();
        setupButtons();
    }

    private void setupTable() {
        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Tiêu đề", "Mô tả", "Số câu", "Ngày tạo"}, 0
        );

        try {
            List<Exam> exams = examDao.findAllWithCategoryAndLevel();
            exams.forEach(exam -> {
                model.addRow(new Object[]{
                        exam.getId(),
                        exam.getTitle(),
                        exam.getDescription(),
                        exam.getQuestionCount(),
                        exam.getCreatedAt()
                });
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải danh sách đề thi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        view.getTblExams().setModel(model);
    }

    private void setupButtons() {
        view.getBtnCreate().addActionListener(e -> createExam());
        view.getBtnDelete().addActionListener(e -> deleteExam());
        view.getBtnFilter().addActionListener(e -> filterExams());
    }

    private void createExam() {
        ExamFormDialog form = new ExamFormDialog(
                (JFrame)SwingUtilities.getWindowAncestor(view),
                categoryDao.findAll(),
                levelDao.findAll()
        );
        form.setVisible(true);
        if (!form.isSaved()) return;

        String title = form.getTitleText();
        String description = form.getDescriptionText();
        String categoryName = form.getSelectedCategory();
        String levelName = form.getSelectedLevel();


        //String categoryName = (String) view.getCbCategory().getSelectedItem();
        //String levelName = (String) view.getCbLevel().getSelectedItem();

        if (categoryName == null || levelName == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn loại và cấp độ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Category category = categoryDao.findByName(categoryName).orElse(null);
            Level level = levelDao.findByName(levelName).orElse(null);

            if (category == null || level == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy loại hoặc cấp độ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy danh sách câu hỏi dựa trên category và level
            List<Question> questions = questionDao.findByCategoryAndLevelWithOptions(category, level);

            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không có câu hỏi nào cho loại và cấp độ này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chọn ngẫu nhiên 5 câu hỏi
            int numberOfQuestions = Math.min(5, questions.size());
            Collections.shuffle(questions);
            List<Question> selectedQuestions = questions.stream()
                    .limit(numberOfQuestions)
                    .collect(Collectors.toList());

            // Tạo đề thi mới
            Exam exam = new Exam();
            exam.setTitle("Đề thi ngẫu nhiên - " + LocalDateTime.now().toString());
            exam.setDescription("Đề thi được tạo tự động từ " + categoryName + " và " + levelName);
            exam.setQuestionCount(numberOfQuestions);
            exam.setCategory(category);
            exam.setLevel(level);

            // Lưu Exam trước để có ID
            examDao.save(exam);

            // Tạo danh sách ExamQuestion để lưu mối quan hệ
            List<ExamQuestion> examQuestions = selectedQuestions.stream()
                    .map(question -> new ExamQuestion(exam, question, selectedQuestions.indexOf(question) + 1))
                    .collect(Collectors.toList());

            exam.setExamQuestions(examQuestions);

            // Cập nhật lại Exam với examQuestions
            examDao.update(exam);

            refreshTable();
            JOptionPane.showMessageDialog(view, "Đề thi đã được tạo thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo đề thi: " + e.getMessage() + ". Kiểm tra dữ liệu hoặc kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExam() {
        int selectedRow = view.getTblExams().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đề thi cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Long id = (Long) view.getTblExams().getModel().getValueAt(selectedRow, 0);
            examDao.delete(id);
            refreshTable();
            JOptionPane.showMessageDialog(view, "Xóa đề thi thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa đề thi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterExams() {
        String categoryName = (String) view.getCbCategory().getSelectedItem();
        String levelName = (String) view.getCbLevel().getSelectedItem();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Tiêu đề", "Mô tả", "Số câu", "Ngày tạo"}, 0
        );

        try {
            List<Exam> exams = examDao.findAllWithCategoryAndLevel();
            exams.stream()
                    .filter(exam -> {
                        if (categoryName == null && levelName == null) return true;
                        boolean categoryMatch = categoryName == null || (exam.getCategory() != null && exam.getCategory().getName().equals(categoryName));
                        boolean levelMatch = levelName == null || (exam.getLevel() != null && exam.getLevel().getName().equals(levelName));
                        return categoryMatch && levelMatch;
                    })
                    .forEach(exam -> {
                        model.addRow(new Object[]{
                                exam.getId(),
                                exam.getTitle(),
                                exam.getDescription(),
                                exam.getQuestionCount(),
                                exam.getCreatedAt()
                        });
                    });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lọc đề thi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        view.getTblExams().setModel(model);
    }
}