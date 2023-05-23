package com.github.Chestaci;

import com.github.Chestaci.model.Comment;
import com.github.Chestaci.model.Post;
import com.google.gson.JsonObject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.github.Chestaci.jdbc.DBOperation.getDBComment;
import static com.github.Chestaci.jdbc.DBOperation.getDBPost;
import static com.github.Chestaci.util.TestUtils.auth;

/**
 * Тесты создания, изменения, удаления постов и комментариев.
 */
@Epic("Тесты создания, изменения, удаления постов и комментариев")
public class APITest extends MyTest {

    private static final String PATH_PARAM_POST = "/wp/v2/posts/";
    private static final String PATH_PARAM_COMMENT = "/wp/v2/comments/";
    private static final String TITLE = "title";
    private static final String TITLE_UPD = "titleUPD";
    private static final String CONTENT = "content";
    private static final String CONTENT_UPD = "contentUPD";
    private static final String COMMENT = "comment";
    private static final String COMMENT_UPD = "commentUPD";
    private static final String TRASH = "trash";
    private Integer postID;
    private Integer commentID;

    /**
     * Тест успешного создания поста.
     */
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест создания поста")
    @Story("Тест успешного создания поста")
    public void postCreationTest() {
        //Создание нового JSON Object
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("status", "publish");
        requestParams.addProperty("title", TITLE);
        requestParams.addProperty("content", CONTENT);

        Response response = auth()
                .contentType(ContentType.JSON)
                .pathParams("rest_route", PATH_PARAM_POST)
                .body(requestParams.toString())
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        //Получение id нового поста
        postID = response.as(Post.class).getId();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки созданного поста
        Post post = getDBPost(postID, TITLE, CONTENT);

        Assertions.assertThat(post.getId()).isEqualTo(postID);
        Assertions.assertThat(post.getTitle().getRaw()).isEqualTo(TITLE);
        Assertions.assertThat(post.getContent().getRaw()).isEqualTo(CONTENT);
    }

    /**
     * Тест успешного изменения поста.
     */
    @Test(dependsOnMethods = {"postCreationTest"})
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест изменения поста")
    @Story("Тест успешного изменения поста")
    public void postUpdateTest() {
        //Создание нового JSON Object
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("id", postID);
        requestParams.addProperty("title", TITLE_UPD);
        requestParams.addProperty("content", CONTENT_UPD);

        Response response = auth()
                .contentType(ContentType.JSON)
                .pathParams("rest_route", PATH_PARAM_POST + postID)
                .body(requestParams.toString())
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки отсутствия поста со старыми параметрами
        Post postOLD = getDBPost(postID, TITLE, CONTENT);

        Assertions.assertThat(postOLD.getId()).isNull();

        //Запрос в БД для проверки изменённого поста
        Post postUPD = getDBPost(postID, TITLE_UPD, CONTENT_UPD);

        Assertions.assertThat(postUPD.getId()).isEqualTo(postID);
        Assertions.assertThat(postUPD.getTitle().getRaw()).isEqualTo(TITLE_UPD);
        Assertions.assertThat(postUPD.getContent().getRaw()).isEqualTo(CONTENT_UPD);
    }

    /**
     * Тест успешного создания комментария.
     */
    @Test(dependsOnMethods = {"postCreationTest"})
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест создания комментария")
    @Story("Тест успешного создания комментария")
    public void commentCreationTest() {
        Response response = auth()
                .contentType(ContentType.URLENC)
                .pathParams("rest_route", PATH_PARAM_COMMENT)
                .formParam("post", postID)
                .formParam("content", COMMENT)
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        //Получение id нового комментария
        commentID = response.as(Comment.class).getId();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки созданного комментария
        Comment comment = getDBComment(commentID, COMMENT);

        Assertions.assertThat(comment.getId()).isEqualTo(commentID);
        Assertions.assertThat(comment.getContent().getRaw()).isEqualTo(COMMENT);
    }

    /**
     * Тест успешного изменения комментария.
     */
    @Test(dependsOnMethods = {"commentCreationTest", "postCreationTest"})
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест изменения комментария")
    @Story("Тест успешного изменения комментария")
    public void commentUpdateTest() {
        Response response = auth()
                .contentType(ContentType.URLENC)
                .pathParams("rest_route", PATH_PARAM_COMMENT + commentID)
                .formParam("post", postID)
                .formParam("content", COMMENT_UPD)
                .when().post("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки отсутствия комментария со старыми параметрами
        Comment commentOLD = getDBComment(commentID, COMMENT);

        Assertions.assertThat(commentOLD.getId()).isNull();

        //Запрос в БД для проверки изменённого комментария
        Comment commentUPD = getDBComment(commentID, COMMENT_UPD);

        Assertions.assertThat(commentUPD.getId()).isEqualTo(commentID);
        Assertions.assertThat(commentUPD.getContent().getRaw()).isEqualTo(COMMENT_UPD);
    }

    /**
     * Тест успешного удаления комментария.
     */
    @Test(dependsOnMethods = {"commentCreationTest", "postCreationTest"})
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест удаления комментария")
    @Story("Тест успешного удаления комментария")
    public void commentDeletionTest() {
        Response response = auth()
                .pathParams("rest_route", PATH_PARAM_COMMENT + commentID)
                .when()
                .delete("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки удалённого комментария
        Comment comment = getDBComment(commentID, COMMENT);

        Assertions.assertThat(comment.getCommentStatus()).isEqualTo(TRASH);
    }

    /**
     * Тест успешного удаления поста.
     */
    @Test(dependsOnMethods = {"postCreationTest"})
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест удаления поста")
    @Story("Тест успешного удаления поста")
    public void postDeletionTest() {
        Response response = auth()
                .pathParams("rest_route", PATH_PARAM_POST + postID)
                .when()
                .delete("?rest_route={rest_route}")
                .then()
                .extract()
                .response();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.contentType()).isEqualTo(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки удалённого поста
        Post post = getDBPost(postID, TITLE, CONTENT);

        Assertions.assertThat(post.getPostStatus()).isEqualTo(TRASH);
    }
}
