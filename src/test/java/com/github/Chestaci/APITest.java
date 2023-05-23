package com.github.Chestaci;

import com.github.Chestaci.model.Comment;
import com.github.Chestaci.model.Content;
import com.github.Chestaci.model.Post;
import com.github.Chestaci.model.Title;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
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

    private Integer postID;
    private Integer commentID;

    /**
     * Тест успешного создания поста.
     */
    @Test(priority = 1)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест создания поста")
    @Story("Тест успешного создания поста")
    public void postCreationTest() {
        //Создание нового поста
        Post createPost = Post.builder().build();

        //Получение id нового поста
        postID = auth()
                .contentType(ContentType.JSON)
                .pathParams("rest_route", PATH_PARAM_POST)
                .body(createPost)
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .extract()
                .path("id");

        //Запрос в БД для проверки созданного поста
        Post post = getDBPost(postID, TITLE, CONTENT);

        Assertions.assertThat(post.getId()).isEqualTo(postID);
        Assertions.assertThat(post.getTitle().getRaw()).isEqualTo(TITLE);
        Assertions.assertThat(post.getContent().getRaw()).isEqualTo(CONTENT);
    }

    /**
     * Тест успешного изменения поста.
     */
    @Test(priority = 2)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест изменения поста")
    @Story("Тест успешного изменения поста")
    public void postUpdateTest() {
        //Создание нового поста
        Post updatePost = Post.builder()
                .id(postID)
                .title(Title.builder().raw(TITLE_UPD).build())
                .content(Content.builder().raw(CONTENT_UPD).build())
                .build();

        auth()
                .contentType(ContentType.JSON)
                .pathParams("rest_route", PATH_PARAM_POST + postID)
                .body(updatePost)
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"));

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
    @Test(priority = 3)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест создания комментария")
    @Story("Тест успешного создания комментария")
    public void commentCreationTest() {
        //Получение id нового комментария
        commentID = auth()
                .contentType(ContentType.URLENC)
                .pathParams("rest_route", PATH_PARAM_COMMENT)
                .formParam("post", postID)
                .formParam("content", COMMENT)
                .when()
                .post("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .extract()
                .path("id");

        //Запрос в БД для проверки созданного комментария
        Comment comment = getDBComment(commentID, COMMENT);

        Assertions.assertThat(comment.getId()).isEqualTo(commentID);
        Assertions.assertThat(comment.getContent().getRaw()).isEqualTo(COMMENT);
    }

    /**
     * Тест успешного изменения комментария.
     */
    @Test(priority = 4)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест изменения комментария")
    @Story("Тест успешного изменения комментария")
    public void commentUpdateTest() {
        auth()
                .contentType(ContentType.URLENC)
                .pathParams("rest_route", PATH_PARAM_COMMENT + commentID)
                .formParam("post", postID)
                .formParam("content", COMMENT_UPD)
                .when().post("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"));

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
    @Test(priority = 5)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест удаления комментария")
    @Story("Тест успешного удаления комментария")
    public void commentDeletionTest() {
        auth()
                .pathParams("rest_route", PATH_PARAM_COMMENT + commentID)
                .when()
                .delete("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки удалённого комментария
        Comment comment = getDBComment(commentID, COMMENT_UPD);

        Assertions.assertThat(comment.getCommentStatus()).isEqualTo(TRASH);
    }

    /**
     * Тест успешного удаления поста.
     */
    @Test(priority = 6)
    @Severity(value = SeverityLevel.NORMAL)
    @Feature("Тест удаления поста")
    @Story("Тест успешного удаления поста")
    public void postDeletionTest() {
        auth()
                .pathParams("rest_route", PATH_PARAM_POST + postID)
                .when()
                .delete("?rest_route={rest_route}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON.withCharset("UTF-8"));

        //Запрос в БД для проверки удалённого поста
        Post post = getDBPost(postID, TITLE_UPD, CONTENT_UPD);

        Assertions.assertThat(post.getPostStatus()).isEqualTo(TRASH);
    }
}
