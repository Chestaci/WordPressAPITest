package com.github.Chestaci.jdbc;

/**
 * Запросы в БД.
 */
public enum SQLQuery {

    QUERY_GET_POST("SELECT * FROM wp_posts WHERE ID = ? AND post_title = ? AND post_content = ?"),
    QUERY_GET_COMMENT("SELECT * FROM wp_comments WHERE comment_ID = ? AND comment_content = ?"),
    QUERY_INSERT_POST("INSERT INTO wp_posts (post_author, post_date, post_date_gmt, post_title, " +
            "post_content, post_status, post_excerpt, to_ping, pinged, post_modified, post_modified_gmt, " +
            "post_content_filtered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    QUERY_INSERT_COMMENT("INSERT INTO wp_comments (comment_post_ID, comment_author, comment_date, comment_date_gmt, comment_content) " +
            "VALUES (?, ?, ?, ?, ?)"),
    QUERY_DELETE_POST("DELETE FROM wp_posts WHERE ID = ?"),
    QUERY_DELETE_COMMENT("DELETE FROM wp_comments WHERE comment_ID = ?");

    public final String QUERY;

    SQLQuery(String QUERY) {
        this.QUERY = QUERY;
    }
}
