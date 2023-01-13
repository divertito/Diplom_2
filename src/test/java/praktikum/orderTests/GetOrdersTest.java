package praktikum.orderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.generator.UserGenerator;
import praktikum.order.OrderClient;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    private OrderClient orderClient;
    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user));
    }

//    Получение всех заказов
//    Получение заказов авторизованного пользователя
//    Получение заказов неавторизованного пользователя
    @Test
    @DisplayName("Get all orders")
    public void getAllOrders() {
        ValidatableResponse response = orderClient.getAllOrders();

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_OK, statusCode);
    }

    @Test
    @DisplayName("Get orders of authorized user")
    public void getAuthorizedUserOrders() {
        String accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = orderClient.getUserOrders(accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_OK, statusCode);
    }

    @Test
    @DisplayName("Get orders of unauthorized user")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse response = orderClient.getUserOrders("");

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_UNAUTHORIZED, statusCode);
    }
}