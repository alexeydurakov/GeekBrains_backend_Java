package ru.alexeydurakov.tests;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.alexeydurakov.dto.Product;
import ru.alexeydurakov.enums.CategoryType;
import ru.alexeydurakov.dto.Category;
import ru.alexeydurakov.service.CategoryService;
import ru.alexeydurakov.service.ProductService;
import ru.alexeydurakov.utils.RetrofitUtils;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;

    Faker faker = new Faker();

    Product product;
    Product productUpdate;
    Product productAdd;
    Integer productId;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();

        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }
    @BeforeEach
    void setUP(){
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

// GET запросы
    //Получение данных о продукте по ID
    @Test
    void getProductIdPositiveTest() throws IOException {
        //Создаем
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        Response<Product> responseGet = productService.getProduct(productId).execute();

        log.info(responseGet.body().toString());
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Получение данных о продукте без ID
    @Test
    void getProductWithoutIdTest() throws IOException {
        Response<ArrayList<Product>> responseGet = productService.getProducts().execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(responseGet.code(),  equalTo(200));
    }

    //Получение данных  о продукте по несуществующему ID, негативный тест
    @Test
    void getProductMoreIdNegativeTest() throws IOException {
        //Создаем
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());

        Response<Product> responseGetMoreId = productService.getProduct(productId + 10000).execute();
        log.info(String.valueOf(responseGetMoreId.code()));

        assertThat(responseGetMoreId.code(), equalTo(404));
        }

//DELETE
    //Удаление продукта по ID
    @Test
    void deleteProductIdNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        log.info(String.valueOf(response.body().getTitle()));
        productService.deleteProduct(productId).execute();
        Response<Product> responseGetDelId = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGetDelId.code()));
        assertThat(responseGetDelId.code(), equalTo(404));
    }

    //Удаление продукта по несуществующему ID
    @Test
    void deleteProductWithoutIdTest() throws IOException {
        //Создаем
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productService.deleteProduct(productId+100).execute();
        Response<Product> responseGetDelId = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGetDelId.code()));
        assertThat(responseGetDelId.code(), equalTo(200));
    }

//PUT
    //Обновление продукта только цена
    @Test
    void putProductChangePriceTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(String.valueOf(response.body().getPrice()));
        log.info(responseUpdate.body().toString());
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение наименования  продукта
    @Test
    void putProductChangeTitleTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getTitle()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice(response.body().getPrice())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(responseUpdate.body().toString());
        log.info(String.valueOf(responseUpdate.body().getTitle()));
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getTitle()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение категории  продукта
    @Test
    void putProductChangeCategoryNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getTitle()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice(response.body().getPrice())
                .withCategoryTitle("NewCategory");
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));

        assertThat(responseUpdate.code(), equalTo(500));

    }

    //Изменение наименования и цены продукта
    @Test
    void putProductChangePriceTitleTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));
        log.info(String.valueOf(response.body().getTitle()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(responseUpdate.body().toString());
        log.info(String.valueOf(responseUpdate.body().getPrice()));
        log.info(String.valueOf(responseUpdate.body().getTitle()));
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        log.info(String.valueOf(responseGet.body().getTitle()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение цены на 0
    @Test
    void putProductChangePriceZeroTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice(0)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(String.valueOf(response.body().getPrice()));
        log.info(responseUpdate.body().toString());
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение цены на дробное
    @Test
    void putProductChangePriceDecimalTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice(124/100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(String.valueOf(response.body().getPrice()));
        log.info(responseUpdate.body().toString());
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение цены на отрицательную
    @Test
    void putProductChangeNegPriceTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice(-100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(String.valueOf(response.body().getPrice()));
        log.info(responseUpdate.body().toString());
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение продута без цены
    @Test
    void putProductChangeWithoutPriceTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getPrice()));

        productUpdate = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(String.valueOf(response.body().getPrice()));
        log.info(responseUpdate.body().toString());
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getPrice()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

    //Изменение продута без наименования
    @Test
    void putProductWithoutTitleTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getTitle()));

        productUpdate = new Product()
                .withId(productId)
                .withPrice(response.body().getPrice())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpdate = productService.updateProduct(productUpdate).execute();
        log.info(String.valueOf(responseUpdate.code()));
        log.info(responseUpdate.body().toString());
        log.info(String.valueOf(responseUpdate.body().getTitle()));
        assertThat(responseUpdate.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpdate.body().getId(), equalTo(productId));

        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        log.info(String.valueOf(responseGet.body().getTitle()));
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));
    }

//POST
//    Добавление продукта со всеми полями
    @Test
    void postProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        log.info(response.body().toString());
        assertThat(response.body().getId(), equalTo(product.getId()));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

//    Добавление продукта без наименования
    @Test
    void postProductWithoutTitleNegativeTest() throws IOException {
        productAdd = new Product()
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(productAdd).execute();

        log.info(String.valueOf(response.code()));
        assertThat(response.code(),  equalTo(201));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        log.info(response.body().toString());
    }

//    Добавление продукта без цены
    @Test
    void postProductWithoutPriceNegativeTest() throws IOException {
        productAdd = new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(productAdd).execute();

        log.info(String.valueOf(response.code()));
        assertThat(response.code(),  equalTo(201));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        log.info(response.body().toString());
    }

//    Добавление продукта без категории
    @Test
    void postProductWithoutCategoryNegativeTest() throws IOException {
        productAdd = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100));

        Response<Product> response = productService.createProduct(productAdd).execute();

        log.info(String.valueOf(response.code()));
        assertThat(response.code(),  equalTo(500));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        log.info(response.body().toString());
    }

//    Добавление продукта c ценой 0
    @Test
    void postProductZeroPriceNegativeTest() throws IOException {
        productAdd = new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(productAdd).execute();

        log.info(String.valueOf(response.code()));
        assertThat(response.code(),  equalTo(201));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        log.info(response.body().toString());
    }

    //   Добавление продукта c отрицательной ценой
    @Test
    void postProductNegPriceNegativeTest() throws IOException {
        productAdd = new Product()
                .withTitle(faker.food().dish())
                .withPrice(-100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(productAdd).execute();

        log.info(String.valueOf(response.code()));
        assertThat(response.code(),  equalTo(201));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        log.info(response.body().toString());
    }

    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();

 //       log.info(response.body().toString());
        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getTitle(), equalTo(id));

    }
}
