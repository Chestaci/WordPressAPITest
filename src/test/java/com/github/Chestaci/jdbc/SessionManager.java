package com.github.Chestaci.jdbc;

import java.sql.Connection;

/**
 * Интерфейс для создания подключения к базе данных с автозакрытием подключения.
 */
public interface SessionManager extends AutoCloseable {

    void beginSession();
    void close();
    Connection getCurrentSession();
}
