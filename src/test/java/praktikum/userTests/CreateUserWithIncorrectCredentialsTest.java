package praktikum.userTests;

import praktikum.generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateUserWithIncorrectCredentialsTest {

    private User user;
    private UserClient userClient;
    public String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user), accessToken);
    }

    //создать пользователя, который уже зарегистрирован
    @Test
    @DisplayName("Creating existed user")
    public void userWithExistedNameCantBeCreated() {
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);
        String actualMessage = response.extract().path("message");
        String expectedMessage = "User already exists";
        assertEquals("Incorrect message on creating user with the same credentials", expectedMessage, actualMessage);
    }
}
