package praktikum.config;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    protected static final String USER_AUTH_PATH = "/auth";
    protected static final String ORDER_PATH = "/orders";
    protected static final String INGREDIENTS = "/ingredients";


    public RequestSpecification getBaseSpec() {
        return given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .baseUri(getBaseUrl());
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getUserAuthPath() {
        return USER_AUTH_PATH;
    }

    public static String getOrderPath() {
        return ORDER_PATH;
    }

    public static String getIngredients() {
        return INGREDIENTS;
    }
}
