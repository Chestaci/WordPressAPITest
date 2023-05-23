package com.github.Chestaci;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

abstract public class MyTest {
    @BeforeSuite
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterSuite
    static void afterAll() {
        RestAssured.reset();
    }
}
