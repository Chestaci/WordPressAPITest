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
}
