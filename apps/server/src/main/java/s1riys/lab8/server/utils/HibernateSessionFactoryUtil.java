package s1riys.lab8.server.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.entities.OrganizationEntity;
import s1riys.lab8.server.entities.ProductEntity;
import s1riys.lab8.server.entities.UserEntity;

import java.util.Properties;

public class HibernateSessionFactoryUtil {
    private static final Logger logger = Main.logger;
    public static Dotenv dotenv = Main.dotenv;
    private static SessionFactory sessionFactory;

    private static Configuration getConfiguration() {
        var url = dotenv.get("DB_URL");
        var user = dotenv.get("DB_USER");
        var password = dotenv.get("DB_PASSWORD");

        String envErrorMessage = null;
        if (url == null || url.isEmpty()) envErrorMessage = "Неверный URL";
        else if (user == null || user.isEmpty()) envErrorMessage = "Неверный USER";
        else if (password == null || password.isEmpty()) envErrorMessage = "Неверный PASSWORD";

        if (envErrorMessage != null) {
            System.out.printf("Ошибка при загрузке переменных окружения %s: %n", envErrorMessage);
            System.exit(1);
        }

        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");

        properties.put("hibernate.connection.username", user);
        properties.put("hibernate.connection.password", password);
        properties.put("hibernate.connection.url", url);

        properties.put("hibernate.connection.pool_size", "100");
        properties.put("hibernate.current_session_context_class", "thread");
        properties.put("hibernate.connection.autocommit", "true");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
        properties.put("hibernate.hbm2ddl.auto", "update");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        return configuration;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = getConfiguration();

            configuration.addAnnotatedClass(ProductEntity.class);
            configuration.addAnnotatedClass(OrganizationEntity.class);
            configuration.addAnnotatedClass(UserEntity.class);

            SessionFactory sessionFactory = configuration.buildSessionFactory();
            logger.info("Hibernate SessionFactory успешно создана");
            return sessionFactory;

        } catch (Exception e) {
            logger.error("При создании Hibernate SessionFactory произошла ошибка", e);
            throw new RuntimeException(e);
            // TODO: add custom exception
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }
}
