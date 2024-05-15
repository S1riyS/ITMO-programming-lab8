package s1riys.lab8.server.managers;

import org.apache.logging.log4j.Logger;
import s1riys.lab8.common.exceptions.MustBeUniqueException;
import s1riys.lab8.common.models.User;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.Services.UserService;
import s1riys.lab8.server.Services.utils.ServiceLocator;
import s1riys.lab8.server.entities.UserEntity;

public class AuthManager {
    private final int SALT_LENGTH = 10;
    private final Logger logger = Main.logger;
    private final UserService userService = (UserService) ServiceLocator.getService("UserService");

    public AuthManager() {

    }

    public Long register(User user) {
        try {
            return userService.add(user);
        } catch (MustBeUniqueException e) {
            return (long) -1;
        }
    }

    public Long login(User user) {
        UserEntity userEntity = userService.findByName(user.getName());

        boolean passwordMatches;
        if (userEntity != null) {
            String expectedPassword = user.getPassword();
            String salt = userEntity.getSalt();
            String realHashedPassword = userEntity.getHashedPassword();
            passwordMatches = userService.comparePasswords(expectedPassword, salt, realHashedPassword);
        } else {
            passwordMatches = false;
        }

        if (userEntity == null || !passwordMatches) return (long) -1;
        return userEntity.getId();
    }
}
