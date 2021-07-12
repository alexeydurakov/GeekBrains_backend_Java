package ru.alexeydurakov.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountTests extends BaseTest{

    @Test
    void getAccountInfoTest() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithLogingTest() {
        given()
                .header("Authorization", token)
                .log()
                .all()
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithAssertionInGivenTest() {
        given()
                .header("Authorization", token)
                .log()
                .all()
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", equalTo(username))
                .body("success", equalTo("true"))
                .body("status", equalTo(200))
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
    }

    @Test
    void getAccountInfoWithAssertionAfterTest() {
        Response response = given()
                .header("Authorization", token)
                .log()
                .all()
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"), equalTo(username));
    }

    private static void getProperties(){
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")){
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
