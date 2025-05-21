package com.example.exambank.service;

import com.example.exambank.dao.CategoryDao;
import com.example.exambank.dao.LevelDao;
import com.example.exambank.dao.QuestionDao;
import com.example.exambank.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.google.gson.stream.JsonReader;
import com.google.gson.GsonBuilder;
import java.io.StringReader;
import java.util.Optional;

public class QuestionImportService {
        @PersistenceContext
        private EntityManager em;
        private final CategoryDao categoryDao;
        private final LevelDao levelDao;
        private final QuestionDao questionDao;

        public void importFromImage(Component parent, String category, String level) {
            // Hiển thị dialog chọn file
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                try {
                    // Gọi OCR service
                    String jsonArray = ImageChatCompletion.processImageArray(
                            fileChooser.getSelectedFile().getPath(),
                            getFileExtension(fileChooser.getSelectedFile().getName())
                    );
                    jsonArray = jsonArray.strip();
                    if (jsonArray.startsWith("```")) {
                        jsonArray = jsonArray.replaceAll("^```(?:json)?\\s*", "")
                                .replaceAll("\\s*```$", "");
                    }
                    if (jsonArray.startsWith("\"") && jsonArray.endsWith("\"")) {
                        jsonArray = jsonArray.substring(1, jsonArray.length()-1)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"");
                    }
                    Gson gson = new GsonBuilder()
                            .setLenient()            // bật lenient mode
                            .create();

                    JsonReader reader = new JsonReader(new StringReader(jsonArray));

                    reader.setLenient(true);     // cho phép JSON “không chuẩn”
                    // Parse JSON
                    List<QuestionData> questions = new Gson().fromJson(
                            reader,
                            new TypeToken<List<QuestionData>>() {
                            }.getType()
                    );

                    // Lấy Category và Level từ UI
                    Category cat = getCategoryByName(category);
                    Level lvl = getLevelByName(level);

                    // Lưu vào database
                    saveQuestions(questions, cat, lvl);

                    JOptionPane.showMessageDialog(parent, "Import thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Lỗi: " + ex.getMessage());
                }
            }
        }

        private Category getCategoryByName(String name) {
            return categoryDao.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found: " + name));
        }

        private Level getLevelByName(String name) {
            return levelDao.findByName(name)
                .orElseThrow(() -> new RuntimeException("Level not found: " + name));
        }

        private void saveQuestions(List<QuestionData> questions, Category category, Level level) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                for (QuestionData q : questions) {
                    Optional<Question> existingQuestion = questionDao.findByName(q.question);
                    if (existingQuestion.isPresent()) {
                        continue; // Bỏ qua nếu câu hỏi đã tồn tại
                    }
                    Question question = new Question();
                    question.setContent(q.question);
                    question.setCategory(category);
                    question.setLevel(level);

                    if (q.options != null) {
                        // Xử lý multiple-choice
                        for (int i = 0; i < q.options.size(); i++) {
                            AnswerOption opt = new AnswerOption();
                            opt.setLabel(Character.toString((char) ('A' + i)));
                            opt.setContent(q.options.get(i));
                            opt.setCorrect(opt.getLabel().equals(q.answer));
                            opt.setQuestion(question);
                            question.getOptions().add(opt);
                        }
                    } else {
                        // Xử lý điền từ
                        question.setSuggestedAnswer(q.answer);
                    }
                    em.persist(question);
                }
                tx.commit();
            } catch (Exception ex) {
                    tx.rollback();
                    throw ex;
                }
        }

        private static class QuestionData {
            String question;
            List<String> options;
            String answer;
        }

        private String getFileExtension(String filename) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        public QuestionImportService(EntityManager em) {
            this.em = em;
            this.categoryDao = new CategoryDao();
            this.levelDao = new LevelDao();
            this.questionDao = new QuestionDao();
        }
}
