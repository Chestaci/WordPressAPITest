package com.github.Chestaci;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.github.Chestaci.jdbc.DBOperation.deleteDBComment;
import static com.github.Chestaci.jdbc.DBOperation.deleteDBPost;
import static com.github.Chestaci.jdbc.DBOperation.insertDBComment;
import static com.github.Chestaci.jdbc.DBOperation.insertDBPost;
import static com.github.Chestaci.util.TestUtils.auth;
import static org.hamcrest.Matchers.equalTo;

/**
 * Тесты получения постов и комментариев.
 */
@Epic("Тесты получения постов и комментариев")
public class GetMethodAPITest extends MyTest{

    private Integer postID;
    private Integer commentID;

    /**
     * Тест успешного получения с помощью GET запроса поста,
     * созданного в БД, с последующим удалением из БД.
     */
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест получения поста")
    @Story("Тест успешного получения поста")
    public void getPostTest(){
        //Получение id нового поста
        postID = insertDBPost();

        auth()
                .pathParams("rest_route", PATH_PARAM_POST + postID)
                .when()
                .get("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body("id", equalTo(postID));

        //Удаление поста из БД и подтверждение о его удалении
        Assertions.assertThat(deleteDBPost(postID)).isEqualTo(1);
    }

    /**
     * Тест успешного получения с помощью GET запроса комментария,
     * созданного в БД, с последующим удалением из БД.
     */
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест получения комментария")
    @Story("Тест успешного получения комментария")
    public void getCommentTest(){
        //Получение id нового поста
        postID = insertDBPost();
        //Получение id нового комментария
        commentID = insertDBComment(postID);

       auth()
                .pathParams("rest_route", PATH_PARAM_COMMENT + commentID)
                .when()
                .get("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body("id", equalTo(commentID));

        //Удаление комментария из БД и подтверждение о его удалении
        Assertions.assertThat(deleteDBComment(commentID)).isEqualTo(1);
        //Удаление поста из БД и подтверждение о его удалении
        Assertions.assertThat(deleteDBPost(postID)).isEqualTo(1);
    }
}
