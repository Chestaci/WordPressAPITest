package com.github.Chestaci.exception;

/**
 * Класс для описания ошибок SessionManager.
 */
public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String message) {
        super(message);
    }

    public SessionManagerException(Throwable cause) {
        super(cause);
    }
}
