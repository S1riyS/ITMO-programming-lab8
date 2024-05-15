package s1riys.lab8.server.dao;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.entities.OrganizationEntity;
import s1riys.lab8.server.entities.UserEntity;
import s1riys.lab8.server.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDAO {
    private final SessionFactory sessionFactory;
    private final Logger logger = Main.logger;

    public UserDAO() {
        this.sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    }

    public Long add(UserEntity newUser) {
        logger.info("Добавление нового пользователя - {}", newUser.getName());

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(newUser);
        session.getTransaction().commit();
        session.close();

        logger.info("Пользователь {} успешно создан", newUser.getName());
        return newUser.getId();
    }

    public UserEntity findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        UserEntity user = session.get(UserEntity.class, id);

        session.getTransaction().commit();
        session.close();

        return user;
    }

    public UserEntity findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var query = session.createQuery("FROM UserEntity WHERE name=:name");
        query.setParameter("name", name);
        List<UserEntity> result = (List<UserEntity>) query.list();

        session.getTransaction().commit();
        session.close();

        if (result.isEmpty()) return null;
        return result.get(0);
    }
}
