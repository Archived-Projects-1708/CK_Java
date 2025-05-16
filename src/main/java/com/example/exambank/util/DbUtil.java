package com.example.exambank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DbUtil {
    private static final String PROPS_FILE = "/config.properties";
    private static String url, user, pass;

    static {
        try (InputStream in = DbUtil.class.getResourceAsStream(PROPS_FILE)) {
            Properties p = new Properties();
            p.load(in);
            url  = p.getProperty("db.url");
            user = p.getProperty("db.user");
            pass = p.getProperty("db.pass");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            throw new RuntimeException("Init DB util failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
