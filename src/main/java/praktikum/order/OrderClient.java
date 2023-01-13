package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.config.Config;


public class OrderClient extends Config {

    @Step("Create new order {order}")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(getOrderPath())
                .then();
    }

    @Step("Get all orders {orders}")
    public ValidatableResponse getAllOrders() {
        return getBaseSpec()
                .get(getOrderPath() + "/all")
                .then();
    }

    @Step("Get user's orders {orders}")
    public ValidatableResponse getUserOrders(String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .get(getOrderPath())
                .then();
    }

    @Step("Get all ingredients {ingredients}")
    public ValidatableResponse getIngredientsList() {
        return getBaseSpec()
                .get(getIngredients())
                .then();
    }
}
