package com.github.Chestaci.util;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * Утилитный класс.
 */
public class TestUtils {

    private TestUtils() {
    }

    @Step("Авторизация")
    public static RequestSpecification auth() {
        return RestAssured.given()
                .baseUri(ConfProperties.getProperty("uri"))
                .auth()
                .preemptive()
                .basic(ConfProperties.getProperty("username"), ConfProperties.getProperty("password"));
    }
}
