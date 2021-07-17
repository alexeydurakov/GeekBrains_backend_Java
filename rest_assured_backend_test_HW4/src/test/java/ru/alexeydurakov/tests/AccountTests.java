package ru.alexeydurakov.tests;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.alexeydurakov.Endpoints.GET_ACCOUNT;

public class AccountTests extends BaseTest{


    @Test
    void getAccountInfoTest() {
        given(requestSpecificationWithAuth)
                .when()
                .get(GET_ACCOUNT, username);
    }

    @Test
    void getAccountInfoWithLogingTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .get(GET_ACCOUNT, username)
                .prettyPeek();
    }

    @Test
    void getAccountInfoWithAssertionInGivenTest() {
        given(requestSpecificationWithAuth, positiveUrlResponseSpecification)
                .get(GET_ACCOUNT, username)
                .prettyPeek();
    }

     @Test
    void getAccountInfoWithAssertionAfterTest() {
        Response response = given(requestSpecificationWithAuth)
                .get(GET_ACCOUNT, username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"), equalTo(username));
    }

    @Test
    void getAccountInfoWithoutTokenTest() {
        ResponseSpecification spec = new ResponseSpecBuilder()
                .expectStatusCode(401)
                .build();
        given(requestSpecificationWithAuth)
                .expect()
                .when()
                .get(GET_ACCOUNT, username);
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
