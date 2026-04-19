package com.example.demo1.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection instance;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private DatabaseConnection() {
        URL = "jdbc:mysql://localhost:3306/cafeteria";
        USER = "root";
        PASSWORD = "";
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        DatabaseConnection.URL = URL;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        DatabaseConnection.USER = USER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        DatabaseConnection.PASSWORD = PASSWORD;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DatabaseConnection instance = new DatabaseConnection();
            return DriverManager.getConnection(instance.getURL(), instance.getUSER(), instance.getPASSWORD());
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver no encontrado", e);
        }
    }
}
