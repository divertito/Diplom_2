package praktikum.generator;

import org.apache.commons.lang3.RandomStringUtils;
import praktikum.user.User;


public class UserGenerator {
    public static User getDefaultUser() {
        return new User("qwerty@qwerty.com", "Qwe1234!", "Vasiliy");
    }

    public static User getRandomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(10) + "@qwerty.com", RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphabetic(9));
    }

}
