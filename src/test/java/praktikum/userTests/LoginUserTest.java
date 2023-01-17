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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    public String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(UserCredentials.from(user), accessToken);
        }
    }

    //    логин под существующим пользователем,
//    логин с неверным логином и паролем.
    @Test
    @DisplayName("Login new user with valid credentials")
    public void userCanBeLoggedIn() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);
        accessToken = loginResponse.extract().path("accessToken");
        assertNotNull("User was not created or token is broken", accessToken);
    }

    @Test
    @DisplayName("Login new user with invalid email")
    public void userWithInvalidEmailCantBeLoggedIn() {
        user.setEmail("qweqwe@blahblah");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);
        accessToken = loginResponse.extract().path("accessToken");
        assertNull("User with incorrect email was logged-in by some reason", accessToken);
    }

    @Test
    @DisplayName("Login new user with invalid password")
    public void userWithInvalidPasswordCantBeLoggedIn() {
        user.setPassword(null);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);
        accessToken = loginResponse.extract().path("accessToken");
        assertNull("User with empty password was logged-in by some reason", accessToken);
    }
}
