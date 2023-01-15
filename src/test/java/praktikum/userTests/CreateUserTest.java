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
import static org.junit.Assert.*;

public class CreateUserTest {
    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user), userClient.login(UserCredentials.from(user)).extract().path("accessToken"));
    }

    //    создать уникального пользователя
    @Test
    @DisplayName("Creating new user with valid credentials")
    public void userCanBeCreated() {
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status is incorrect", SC_OK, statusCode);
        boolean isCreated = response.extract().path("success");
        assertTrue("User is not created", isCreated);
    }

}
