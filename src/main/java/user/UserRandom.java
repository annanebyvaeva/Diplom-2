package user;

import model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserRandom {
    public static User getRandom(){
        User user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(8)+"@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(8));
        user.setName(RandomStringUtils.randomAlphabetic(8));
        return user;
    }
}
