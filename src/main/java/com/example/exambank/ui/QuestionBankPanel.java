package com.example.exambank.ui;

import com.example.exambank.service.QuestionImportService;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// 2. QuestionBankPanel: quản lý CRUD câu hỏi, filter theo loại và cấp độ, + import ảnh
public class QuestionBankPanel extends JPanel {
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbLevel;
    private JButton btnFilter;
    private JTable tblQuestions;
    private JButton btnAdd, btnEdit, btnDelete;
    private JButton btnImportImage;

    private QuestionImportService importService;

    public QuestionBankPanel(QuestionImportService importService) {
        this.importService = importService;
        cbCategory     = new JComboBox<>();
        cbLevel        = new JComboBox<>();
        btnFilter      = new JButton("Lọc");
        tblQuestions   = new JTable();
        btnAdd         = new JButton("Thêm");
        btnEdit        = new JButton("Sửa");
        btnDelete      = new JButton("Xóa");
        btnImportImage = new JButton("Nhập từ ảnh");

        setLayout(new BorderLayout(10, 10));

        // Top: filter panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST; // Căn trái


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

        add(new JScrollPane(tblQuestions), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.add(Box.createHorizontalGlue());  // Đẩy nút sang phải
        actionPanel.add(btnImportImage);
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);
        add(filterPanel, BorderLayout.NORTH);

        // Add import action with loading dialog
        btnImportImage.addActionListener(e -> {
            String selectedCategory = (String) cbCategory.getSelectedItem();
            String selectedLevel    = (String) cbLevel.getSelectedItem();

            if (selectedCategory == null || selectedLevel == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Category và Level");
                return;
            }

            // Prepare loading dialog
            JDialog loadingDialog = new JDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Đang xử lý...",
                    Dialog.ModalityType.APPLICATION_MODAL
            );
            loadingDialog.add(new JLabel("Vui lòng chờ, hệ thống đang phân tích ảnh..."));
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(this);

            // SwingWorker for background import
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    importService.importFromImage(
                            QuestionBankPanel.this,
                            selectedCategory,
                            selectedLevel
                    );
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    try {
                        get(); // rethrow exceptions
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                QuestionBankPanel.this,
                                "Lỗi khi import: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            // Execute and show dialog
            worker.execute();
            loadingDialog.setVisible(true);
        });
    }

    /**
     * Load dữ liệu Category và Level vào combobox.
     */
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

    // Getters for controller wiring
    public JComboBox<String> getCbCategory() { return cbCategory; }
    public JComboBox<String> getCbLevel()    { return cbLevel; }
    public JButton getBtnFilter()            { return btnFilter; }
    public JTable getTblQuestions()          { return tblQuestions; }
    public JButton getBtnAdd()               { return btnAdd; }
    public JButton getBtnEdit()              { return btnEdit; }
    public JButton getBtnDelete()            { return btnDelete; }
    public JButton getBtnImportImage()       { return btnImportImage; }
}
