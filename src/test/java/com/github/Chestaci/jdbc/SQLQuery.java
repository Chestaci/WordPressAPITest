package com.github.Chestaci.jdbc;

/**
 * Запросы в БД.
 */
public enum SQLQuery {

    QUERY_GET_POST("SELECT * FROM wp_posts WHERE ID = ? AND post_title = ? AND post_content = ?"),
    QUERY_GET_COMMENT("SELECT * FROM wp_comments WHERE comment_ID = ? AND comment_content = ?");

    public final String QUERY;

    SQLQuery(String QUERY) {
        this.QUERY = QUERY;
    }
}
