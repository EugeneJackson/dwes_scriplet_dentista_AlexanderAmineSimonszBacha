package org.espais.dentista.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/dentista?useSSL=false&serverTimezone=UTC";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
