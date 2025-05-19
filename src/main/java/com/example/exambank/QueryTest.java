package com.example.exambank;

import java.sql.*;

public class QueryTest {
    public static void main(String[] args) {
        String url      = "jdbc:sqlserver://DESKTOP-9U8KHKR:1433;" +
                "databaseName=JapaneseExamBank;" +
                "encrypt=false;trustServerCertificate=true";
        String user     = "sa";
        String password = "123";

        // Chỉ lấy đúng các cột tồn tại trong categories
        String sql = "SELECT id, name, description FROM categories";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            System.out.println("=== Categories Test ===");
            while (rs.next()) {
                long   id          = rs.getLong("id");
                String name        = rs.getString("name");
                String description = rs.getString("description");

                System.out.printf(
                        "id=%d, name=%s, description=%s%n",
                        id, name, description
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
