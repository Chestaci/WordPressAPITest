package com.github.Chestaci.jdbc;

import com.github.Chestaci.model.Comment;
import com.github.Chestaci.model.Content;
import com.github.Chestaci.model.Post;
import com.github.Chestaci.model.Title;
import com.github.Chestaci.util.ConfProperties;
import io.qameta.allure.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Осуществление запросов в БД.
 */
public class DBOperation {

    /**
     * Метод для осуществления запроса в БД для получения поста.
     *
     * @param id id поста
     * @param postTitle заготовок поста
     * @param postContent содержание поста
     * @return Post
     * @see Post
     */
    @Step("Запрос в БД для получения поста с параметрами: id = {id}, postTitle = {postTitle}, postContent = {postContent}")
    public static Post getDBPost(Integer id, String postTitle, String postContent) {
        Post post = Post.builder().build();

        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_GET_POST.QUERY)) {
                    pst.setInt(1, id);
                    pst.setString(2, postTitle);
                    pst.setString(3, postContent);

                    try (ResultSet resultSet = pst.executeQuery()) {
                        if (resultSet.next()) {
                            post = Post.builder()
                                    .id(resultSet.getInt("ID"))
                                    .title(Title.builder().raw(resultSet.getString("post_title")).build())
                                    .content(Content.builder().raw(resultSet.getString("post_content")).build())
                                    .postStatus(resultSet.getString("post_status"))
                                    .build();
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return post;
    }

    /**
     * Метод для осуществления запроса в БД для получения комментария.
     *
     * @param id id комментария
     * @param commentContent содержание комментария
     * @return Comment
     * @see Comment
     */
    @Step("Запрос в БД для получения комментария с параметрами: id = {id}, commentContent = {commentContent}")
    public static Comment getDBComment(Integer id, String commentContent) {
        Comment comment = Comment.builder().build();

        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_GET_COMMENT.QUERY)) {
                    pst.setInt(1, id);
                    pst.setString(2, commentContent);

                    try (ResultSet resultSet = pst.executeQuery()) {
                        if (resultSet.next()) {
                            comment = Comment.builder()
                                    .id(resultSet.getInt("comment_ID"))
                                    .content(Content.builder().raw(resultSet.getString("comment_content")).build())
                                    .commentStatus(resultSet.getString("comment_approved"))
                                    .build();
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return comment;
    }

    /**
     * Метод для осуществления запроса в БД для создания нового поста.
     *
     * @return id созданного поста
     */
    @Step("Запрос в БД для создания нового поста")
    public static Integer insertDBPost() {
        Integer id = null;
        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_INSERT_POST.QUERY, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, 1);
                    pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setString(4, "title");
                    pst.setString(5, "content");
                    pst.setString(6, "publish");
                    pst.setString(7, "");
                    pst.setString(8, "");
                    pst.setString(9, "");
                    pst.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setString(12, "");

                    pst.executeUpdate();

                    try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            id = generatedKeys.getInt(1);
                        }
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }

    /**
     * Метод для осуществления запроса в БД для создания нового комментария.
     *
     * @return id созданного комментария
     */
    @Step("Запрос в БД для создания нового комментария")
    public static Integer insertDBComment(Integer postID) {
        Integer id = null;
        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_INSERT_COMMENT.QUERY, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, postID);
                    pst.setString(2, "Anastasia.Petrova");
                    pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setString(5, "comment");

                    pst.executeUpdate();

                    try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            id = generatedKeys.getInt(1);
                        }
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }

    /**
     * Метод для осуществления запроса в БД для удаления поста по id.
     *
     * @param id id поста
     * @return количество удалённых строк
     */
    @Step("Запрос в БД для удаления поста по id: {id}")
    public static int deleteDBPost(Integer id) {
        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_DELETE_POST.QUERY)) {
                    pst.setInt(1, id);
                    return pst.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Метод для осуществления запроса в БД для удаления комментария по id.
     *
     * @param id id комментария
     * @return количество удалённых строк
     */
    @Step("Запрос в БД для удаления комментария по id: {id}")
    public static int deleteDBComment(Integer id) {
        try (SessionManager sessionManager = new SessionManagerJDBC(
                ConfProperties.getProperty("db.url"),
                ConfProperties.getProperty("db.username"),
                ConfProperties.getProperty("db.password"),
                ConfProperties.getProperty("db.driver"))) {

            sessionManager.beginSession();

            try (Connection connection = sessionManager.getCurrentSession()) {
                try (PreparedStatement pst = connection.prepareStatement(SQLQuery.QUERY_DELETE_COMMENT.QUERY)) {
                    pst.setInt(1, id);
                    return pst.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }
}
