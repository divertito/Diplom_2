package praktikum.userTests;

import praktikum.generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUsedDataTest {

    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user));
    }

    //    Изменение данных пользователя с авторизацией,
//    Изменение данных пользователя без авторизации,
    @Test
    @DisplayName("Updating data of the logged user")
    public void authorizedUserCanBeUpdated() {
        String accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        user.setPassword("123");
        user.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@qwerty.com");
        user.setName("Ivan");
        ValidatableResponse response = userClient.update(user, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_OK, statusCode);
        boolean isUpdated = response.extract().path("success");
        assertTrue("User is not updated", isUpdated);
    }

    @Test
    @DisplayName("Updating data of the logged user")
    public void unauthorizedUserCantBeUpdated() {
        user.setPassword("123");
        user.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@qwerty.com");
        user.setName("Ivan");
        ValidatableResponse response = userClient.update(user, "");

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_UNAUTHORIZED, statusCode);
        String actual = response.extract().path("message");
        String expected = "You should be authorised";
        assertEquals("Message on try to update unauthorized user data is incorrect", expected, actual);
    }
}
