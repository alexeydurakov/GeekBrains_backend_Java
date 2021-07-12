package ru.alexeydurakov.tests;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class ImageTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/500kb2560_1440.jpg";
    private String path_to_image_url = "https://coverfiles.alphacoders.com/714/thumb-1920-71486.jpg";
    static String encodedfile;
    String uploadedImageId;

    @BeforeEach
    void beforeTest(){
        byte[] byteAttay = getFileContent();
        encodedfile = Base64.getEncoder().encodeToString(byteAttay);
    }
    //загрузка oчень маленькой картинки
    @Test
    void uploadVerySmollFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/1kb.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(300))
                .body("data.height", equalTo(240))
                .body("data.size", lessThan(2500))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка 10Мв картинки
    @Test
    void upload10MbFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/10mb.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.size", lessThan(10485760))
                .body("data.animated",equalTo(false))
                .body("data.vote",is(null))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    //загрузка больше 10Мв картинки
    @Test
    void uploadMore10MbFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/15mb.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.size", lessThan(15728640))
                .body("data.animated",equalTo(false))
                .body("data.vote",is(null))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка c разрешением 600*480 px
    @Test
    void uploadResolution600x480pxFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb600_400.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(600))
                .body("data.height", equalTo(480))
                .body("data.size", lessThan(5120))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка каринки 2560* 1440 px
    @Test
    void uploadResolution2560x1440pxFileTest() {
        uploadedImageId = given()
                .headers("Autorisation", token)
                .multiPart("image", new File("src/test/resources/500kb2560_1440.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(2560))
                .body("data.height", equalTo(1440))
                .body("data.size", lessThan(53248))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    //загрузка c разрешением 5120*2880 px
    @Test
    void uploadResolution5120x2880pxFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb5120_2880.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(5120))
                .body("data.height", equalTo(2880))
                .body("data.size", lessThan(214016))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка c разрешением 7680*4320 px
    @Test
    void uploadResolution7680x4320pxFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb7680_4320.jpg"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(7680))
                .body("data.height", equalTo(7320))
                .body("data.size", lessThan(467968))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    //загрузка gif
    @Test
    void uploadGifFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb7680_4320.gif"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(7680))
                .body("data.height", equalTo(4320))
                .body("data.type", equalTo("image/gif"))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка png
    @Test
    void uploadPngFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb7680_4320.png"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.width", equalTo(7680))
                .body("data.height", equalTo(4320))
                .body("data.type", equalTo("image/jpeg"))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка webp
    @Test
    void uploadWebpFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/500kb7680_4320.webp"))
                .expect()
                .statusCode(400)
                .body("success", is(false))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка видео вместо картинки
    @Test
    void uploadVideoFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/video.mp4"))
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("video/mp4"))
                .body("data.mp4", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    //загрузка каринки Base64
    @Test
    void uploadFileTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", encodedfile)
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    //загрузка каринки URL
    @Test
    void uploadFileUrlTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", path_to_image_url)
                .expect()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("ad_url", equalTo(""))
                .body("name", equalTo(""))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    private byte[] getFileContent() {
        byte[] byteAttay = new byte[0];
        try {
            byteAttay = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteAttay;
    }

}
