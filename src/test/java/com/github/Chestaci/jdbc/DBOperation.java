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

/**
 * Осуществление запросов в БД.
 */
public class DBOperation {

    @Step("Запрос в БД для получения поста с параметрами: id = {id}, postTitle = {postTitle}, postContent = {postContent}")
    public static Post getDBPost(Integer id, String postTitle, String postContent) {
        Post post = new Post();
        Title title = new Title();
        Content content = new Content();
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
                            post.setId(resultSet.getInt("ID"));
                            title.setRaw(resultSet.getString("post_title"));
                            post.setTitle(title);
                            content.setRaw(resultSet.getString("post_content"));
                            post.setContent(content);
                            post.setPostStatus(resultSet.getString("post_status"));
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return post;
    }

    @Step("Запрос в БД для получения комментария с параметрами: id = {id}, commentContent = {commentContent}")
    public static Comment getDBComment(Integer id, String commentContent) {
        Comment comment = new Comment();
        Content content = new Content();
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
                            comment.setId(resultSet.getInt("comment_ID"));
                            content.setRaw(resultSet.getString("comment_content"));
                            comment.setContent(content);
                            comment.setCommentStatus(resultSet.getString("comment_approved"));
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return comment;
    }
}
