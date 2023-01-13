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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.*;

public class CreateUserWithEmptyCredentialsTest {
    private User user;
    private UserClient userClient;
    public String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(UserCredentials.from(user));
        }
    }

    //создать пользователя и не заполнить одно из обязательных полей
    @Test
    @DisplayName("Creating new user with empty email")
    public void userWithEmptyEmailCantBeCreated() {
        user.setEmail(null);
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_FORBIDDEN, statusCode);
        String actual = response.extract().path("message");
        String expected = "Email, password and name are required fields";
        assertEquals("User is created with empty email!", expected, actual);
    }

    @Test
    @DisplayName("Creating new user with empty password")
    public void userWithEmptyPasswordCantBeCreated() {
        user.setPassword(null);
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_FORBIDDEN, statusCode);
        String actual = response.extract().path("message");
        String expected = "Email, password and name are required fields";
        assertEquals("User is created with empty password!", expected, actual);
    }

    @Test
    @DisplayName("Creating new user with empty name")
    public void userWithEmptyNameCantBeCreated() {
        user.setName(null);
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_FORBIDDEN, statusCode);
        String actual = response.extract().path("message");
        String expected = "Email, password and name are required fields";
        assertEquals("User is created with empty name!", expected, actual);
    }
}
