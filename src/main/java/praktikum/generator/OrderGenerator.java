package praktikum.generator;

import io.restassured.response.ValidatableResponse;
import praktikum.order.Order;
import praktikum.order.OrderClient;

import java.util.List;
import java.util.Map;

public class OrderGenerator {
    private static OrderClient orderClient = new OrderClient();

    public static Order getDefaultOrder() {
        return new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa6f"});
    }

    public static Order getRandomOrder() {
        ValidatableResponse response = orderClient.getIngredientsList();

        List<Map<String, String>> ordersList = response.extract().path("data");
        int length = ordersList.size();
        int randomInt = (int) (Math.random() * length);
        String id = ordersList.get(randomInt).get("_id");

        return new Order(new String[]{id});
    }
}

