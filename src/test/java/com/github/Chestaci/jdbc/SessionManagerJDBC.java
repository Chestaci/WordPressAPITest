package com.github.Chestaci.jdbc;

import com.github.Chestaci.exception.SessionManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для создания подключения к базе данных.
 */
public class SessionManagerJDBC implements SessionManager {

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    public SessionManagerJDBC(String url, String username, String password, String driver) {

        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beginSession() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SessionManagerException("Ошибка создания connection");
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public Connection getCurrentSession() {
        return connection;
    }
}
