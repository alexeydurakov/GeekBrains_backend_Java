package ru.alexeydurakov.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alexeydurakov.Endpoints;
import ru.alexeydurakov.dto.PostImageResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static ru.alexeydurakov.Endpoints.UPLOAD_IMAGE;

public class ImageTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/500kb2560_1440.jpg";
    static String encodedfile;
    String uploadedImageId;

    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFileBase;
    MultiPartSpecification multiPartSpecWithFileSmall;
    MultiPartSpecification multiPartSpecWithFileMore10Mb;
    MultiPartSpecification multiPartSpecWithFile10Mb;
    MultiPartSpecification multiPartSpecWithFile600x480px;
    MultiPartSpecification multiPartSpecWithFile2560x1440px;
    MultiPartSpecification multiPartSpecWithFile5120x2880px;
    MultiPartSpecification multiPartSpecWithFile7680x4320px;
    MultiPartSpecification multiPartSpecWithFileGif;
    MultiPartSpecification multiPartSpecWithFilePng;
    MultiPartSpecification multiPartSpecWithFileWebp;
    MultiPartSpecification multiPartSpecWithFileVideo;

    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImageSmall;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImageMore10Mb;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage10Mb;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage600x480px;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage2560x1440px;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage5120x2880px;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImage7680x4320px;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImageGif;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImagePng;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImageWebp;
    static RequestSpecification requestSpecificationWithAuthAndMultiPathImageVideo;

    private final String nameFileBase = "500kb2560_1440.jpg";
    private final String nameFileSmall = "1kb.jpg";
    private final String nameFile15mb = "15mb.jpg";
    private final String nameFile10mb = "10mb.jpg";
    private final String nameFile600x480px = "500kb600_400.jpg";
    private final String nameFile5120x2880px = "500kb5120_2880.jpg";
    private final String nameFile7680x4320px = "500kb7680_4320.jpg";
    private final String nameFileGif = "500kb7680_4320.gif";
    private final String nameFilePng = "500kb7680_4320.png";
    private final String nameFileWebp = "500kb7680_4320.webp";
    private final String nameFileVideo = "withoutregistration.mp4";

    @BeforeEach
    void beforeTest(){

// для теста uploadFileTest
        byte[] byteAttay = getFileContent();
        encodedfile = Base64.getEncoder().encodeToString(byteAttay);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedfile)
                .controlName("image")
                .build();
        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();

// для теста uploadFileImageTest
        multiPartSpecWithFileBase = new MultiPartSpecBuilder(new File(path_resurces + nameFileBase))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFileBase)
                .addFormParam("title", "Base picture")
                .addMultiPart(multiPartSpecWithFileBase)
                .build();

// для теста uploadVerySmallFileTest
        multiPartSpecWithFileSmall = new MultiPartSpecBuilder(new File(path_resurces + nameFileSmall))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImageSmall = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFileSmall)
                .addFormParam("title", "Small picture")
                .addMultiPart(multiPartSpecWithFileSmall)
                .build();

// для теста uploadMore10MbFileTest
        multiPartSpecWithFileMore10Mb = new MultiPartSpecBuilder(new File(path_resurces + nameFile15mb))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImageMore10Mb = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFile15mb)
                .addFormParam("title", "15mb picture")
                .addMultiPart(multiPartSpecWithFileMore10Mb)
                .build();

// для теста upload10MbFileTest
        multiPartSpecWithFile10Mb = new MultiPartSpecBuilder(new File(path_resurces + nameFile10mb))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage10Mb = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFile10mb)
                .addFormParam("title", "10 mb picture")
                .addMultiPart(multiPartSpecWithFile10Mb)
                .build();

// для теста uploadResolution600x480pxFileTest
        multiPartSpecWithFile600x480px = new MultiPartSpecBuilder(new File(path_resurces + nameFile600x480px))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage600x480px = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFile600x480px)
                .addFormParam("title", "600x480px picture")
                .addMultiPart(multiPartSpecWithFile600x480px)
                .build();

// для теста uploadResolution2560x1440pxFileTest
        multiPartSpecWithFile2560x1440px = new MultiPartSpecBuilder(new File(path_resurces + nameFileBase))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage2560x1440px = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFileBase)
                .addFormParam("title", "2560x1440px picture")
                .addMultiPart(multiPartSpecWithFile2560x1440px)
                .build();

// для теста uploadResolution5120x2880pxFileTest
        multiPartSpecWithFile5120x2880px = new MultiPartSpecBuilder(new File(path_resurces + nameFile5120x2880px))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage5120x2880px = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFile5120x2880px)
                .addFormParam("title", "5120x2880px picture")
                .addMultiPart(multiPartSpecWithFile5120x2880px)
                .build();

// для теста uploadResolution7680x4320pxFileTest
        multiPartSpecWithFile7680x4320px = new MultiPartSpecBuilder(new File(path_resurces + nameFile7680x4320px))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImage7680x4320px = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "jpeg")
                .addFormParam("name", nameFile7680x4320px)
                .addFormParam("title", "7680x4320px picture")
                .addMultiPart(multiPartSpecWithFile7680x4320px)
                .build();

// для теста uploadGifFileTest
        multiPartSpecWithFileGif = new MultiPartSpecBuilder(new File(path_resurces + nameFileGif))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImageGif = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "gif")
                .addFormParam("name", nameFileGif)
                .addFormParam("title", "7680x4320px picture")
                .addMultiPart(multiPartSpecWithFileGif)
                .build();

// для теста uploadPngFileTest
        multiPartSpecWithFilePng = new MultiPartSpecBuilder(new File(path_resurces + nameFilePng))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImagePng = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "png")
                .addFormParam("name", nameFilePng)
                .addFormParam("title", "7680x4320px picture")
                .addMultiPart(multiPartSpecWithFilePng)
                .build();

// для теста uploadWebpFileTest
        multiPartSpecWithFileWebp = new MultiPartSpecBuilder(new File(path_resurces + nameFileWebp))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImageWebp = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "webp")
                .addFormParam("name", nameFileWebp)
                .addFormParam("title", "7680x4320px picture")
                .addMultiPart(multiPartSpecWithFileWebp)
                .build();

// для теста uploadWebpFileTest
        multiPartSpecWithFileVideo = new MultiPartSpecBuilder(new File(path_resurces + nameFileVideo))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPathImageVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "webp")
                .addFormParam("name", nameFileVideo)
                .addFormParam("title", "video")
                .addMultiPart(multiPartSpecWithFileVideo)
                .build();
    }


    //загрузка oчень маленькой картинки
    @Test
    void uploadVerySmallFileTest() {
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImageSmall, positiveSmallFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImageMore10Mb, positiveMore10mbFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage10Mb, positive10mbFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage600x480px, positive600x480pxFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage2560x1440px, positive2560x1440pFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage5120x2880px, positive5120x2880pFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage7680x4320px, positive7680x4320pFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImageGif, positiveGifFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImagePng,positivePngFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImageWebp, negativeWebpFileResponseSpecification)
                .post(UPLOAD_IMAGE)
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
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImageVideo, positiveVideoFileResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    @Test
    void uploadFileTest() {
        uploadedImageId = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void deleteFileAuthTest(){
        given(requestSpecificationWithAuth, positiveDeleteFileResponseSpecification)
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void uploadFileImageTest() {
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage)
                .expect()
                .statusCode(200)
                .when()
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @Test
    void uploadWithMultiPath(){
        uploadedImageId = given(requestSpecificationWithAuthAndMultiPathImage)
                .post(UPLOAD_IMAGE)
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
