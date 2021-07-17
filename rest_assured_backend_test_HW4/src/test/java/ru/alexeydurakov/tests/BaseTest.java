package ru.alexeydurakov.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;
    static String path_resurces;

    static ResponseSpecification positiveResponseSpecification;
    static ResponseSpecification positiveUrlResponseSpecification;
    static ResponseSpecification positiveSmallFileResponseSpecification;
    static ResponseSpecification positiveMore10mbFileResponseSpecification;
    static ResponseSpecification positive10mbFileResponseSpecification;
    static ResponseSpecification positive600x480pxFileResponseSpecification;
    static ResponseSpecification positive2560x1440pFileResponseSpecification;
    static ResponseSpecification positive5120x2880pFileResponseSpecification;
    static ResponseSpecification positive7680x4320pFileResponseSpecification;
    static ResponseSpecification positiveGifFileResponseSpecification;
    static ResponseSpecification positivePngFileResponseSpecification;
    static ResponseSpecification negativeWebpFileResponseSpecification;
    static ResponseSpecification positiveVideoFileResponseSpecification;
    static ResponseSpecification positiveDeleteFileResponseSpecification;
    static RequestSpecification requestSpecificationWithAuth;


    @BeforeAll
    static void beforeAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");
        path_resurces = properties.getProperty("path_resurces");

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectResponseTime(lessThanOrEqualTo(12000L))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveUrlResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.url", equalTo(username))
                .expectStatusCode(200)
                .build();

        positiveSmallFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(300))
                .expectBody("data.height", equalTo(240))
                .expectBody("data.size", lessThan(2500))
                .expectStatusCode(200)
                .build();

        positiveMore10mbFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(7680))
                .expectBody("data.height", equalTo(4320))
                .expectBody("data.size", lessThan(15728640))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positive10mbFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(7680))
                .expectBody("data.height", equalTo(4320))
                .expectBody("data.size", lessThan(10485760))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positive600x480pxFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(600))
                .expectBody("data.height", equalTo(480))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positive2560x1440pFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(2560))
                .expectBody("data.height", equalTo(1440))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positive5120x2880pFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(5120))
                .expectBody("data.height", equalTo(2880))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positive7680x4320pFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(7680))
                .expectBody("data.height", equalTo(4320))
                .expectBody("data.animated", equalTo(false))
                .expectBody("data.vote", is(null))
                .expectStatusCode(200)
                .build();

        positiveGifFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(7680))
                .expectBody("data.height", equalTo(4320))
                .expectBody("data.type", equalTo("image/gif"))
                .expectStatusCode(200)
                .build();

        positivePngFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.width", equalTo(7680))
                .expectBody("data.height", equalTo(4320))
                .expectBody("data.type", equalTo("image/jpeg"))
                .expectStatusCode(200)
                .build();

        negativeWebpFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(400))
                .expectStatusCode(400)
                .build();

        positiveVideoFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.type", equalTo("video/mp4"))
                .expectBody("data.mp4", is(notNullValue()))
                .expectStatusCode(200)
                .build();

        positiveDeleteFileResponseSpecification = new ResponseSpecBuilder()
                .expectBody("data", equalTo(true))
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();


    //    RestAssured.responseSpecification = positiveResponseSpecification;
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
