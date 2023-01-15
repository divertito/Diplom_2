package praktikum.orderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.generator.OrderGenerator;
import praktikum.generator.UserGenerator;
import praktikum.order.Order;
import praktikum.order.OrderClient;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    private User user;
    private UserClient userClient = new UserClient();
    private Order order;
    private OrderClient orderClient;
    String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient.create(user);
        order = OrderGenerator.getRandomOrder();
        orderClient = new OrderClient();
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user), accessToken);
    }

    //    Создание заказа с авторизацией с ингредиентами
//    Создание заказа без авторизации
//    Создание заказа с авторизацией без ингредиентов
//    Создание заказа с неверным хешем ингредиентов
    @Test
    @DisplayName("Creating new order with authorized user")
    public void authorizedUserOrderCanBeCreated() {
        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_OK, statusCode);
        boolean isCreated = response.extract().path("success");
        assertTrue("Order is not created", isCreated);
    }

    @Test
    @DisplayName("Creating new order with un-authorized user")
    public void unAuthorizedUserOrderCantBeCreated() {
        ValidatableResponse response = orderClient.createOrder(order, "");

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_UNAUTHORIZED, statusCode);
    }


    @Test
    @DisplayName("Creating new order with authorized user <without ingredients>")
    public void orderWithoutIngredientsCantBeCreated() {
        order.setIngredients(null);

        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_BAD_REQUEST, statusCode);
        String actual = response.extract().path("message");
        String expected = "Ingredient ids must be provided";
        assertEquals("Order is created with without ingredients!", expected, actual);
    }

    @Test
    @DisplayName("Creating new order with authorized user <with broken hash of ingredients>")
    public void orderWithBrokenHashIngredientsCantBeCreated() {
        order.setIngredients(new String[]{"123"});
        ValidatableResponse response = orderClient.createOrder(order, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}
