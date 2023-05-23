package com.github.Chestaci;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

abstract public class MyTest {

    protected static final String PATH_PARAM_POST = "/wp/v2/posts/";
    protected static final String PATH_PARAM_COMMENT = "/wp/v2/comments/";
    protected static final String TITLE = "title";
    protected static final String TITLE_UPD = "titleUPD";
    protected static final String CONTENT = "content";
    protected static final String CONTENT_UPD = "contentUPD";
    protected static final String COMMENT = "comment";
    protected static final String COMMENT_UPD = "commentUPD";
    protected static final String TRASH = "trash";

    @BeforeSuite
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterSuite
    static void afterAll() {
        RestAssured.reset();
    }
}
