package s1riys.lab8.server.Services;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import s1riys.lab8.common.exceptions.MustBeUniqueException;
import s1riys.lab8.common.models.User;
import s1riys.lab8.server.dao.UserDAO;
import s1riys.lab8.server.entities.UserEntity;

import java.nio.charset.StandardCharsets;

public class UserService implements Service {
    private final int SALT_LENGTH = 10;
    private final UserDAO userDAO = new UserDAO();

    public UserService() {
    }

    public Long add(User user) throws MustBeUniqueException {
        boolean userExists = (findByName(user.getName()) != null);
        if (userExists) throw new MustBeUniqueException();

        String salt = generateSalt();
        String hashedPassword = generateHashedPassword(user.getPassword(), salt);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setHashedPassword(hashedPassword);
        userEntity.setSalt(salt);

        return userDAO.add(userEntity);
    }

    public UserEntity findById(Long id) {
        return userDAO.findById(id);
    }

    public UserEntity findByName(String name) {
        return userDAO.findByName(name);
    }

    public boolean comparePasswords(String expectedPassword, String salt, String realHashedPassword) {
        String expectedHashedPassword = generateHashedPassword(expectedPassword, salt);
        return expectedHashedPassword.equals(realHashedPassword);
    }

    private String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
    }

    private String generateHashedPassword(String password, String salt) {
        return Hashing.sha256()
                .hashString(password + salt, StandardCharsets.UTF_8)
                .toString();
    }

    @Override
    public String getName() {
        return "UserService";
    }
}
