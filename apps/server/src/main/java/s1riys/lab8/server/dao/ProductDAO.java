package s1riys.lab8.server.dao;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.entities.ProductEntity;
import s1riys.lab8.server.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ProductDAO {
    private final SessionFactory sessionFactory;
    private final Logger logger = Main.logger;

    public ProductDAO() {
        this.sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    }

    public Long add(ProductEntity newProduct) {
        logger.info("Добавление нового продукта - {}", newProduct.getName());

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(newProduct);
        session.getTransaction().commit();
        session.close();

        logger.info("Продукт с id={} успешно создан", newProduct.getId());
        return newProduct.getId();
    }

    public ProductEntity findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        ProductEntity product = session.get(ProductEntity.class, id);

        session.getTransaction().commit();
        session.close();

        return product;
    }

    public List<ProductEntity> findAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Query<ProductEntity> query = session.createQuery("from ProductEntity");
        List<ProductEntity> result = query.list();

        session.getTransaction().commit();
        session.close();
        return result;
    }

    public void update(ProductEntity updatedProduct) {
        logger.info("Обновление продукта с id={}", updatedProduct.getId());

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.update(updatedProduct);

        session.getTransaction().commit();
        session.close();

        logger.info("Обновление продукта с id={} завершено", updatedProduct.getId());
    }

    public int removeById(Long id) {
        logger.info("Удаление продукта с id={}", id);

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var query = session.createQuery("DELETE FROM ProductEntity WHERE id = :id");
        query.setParameter("id", id);

        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        logger.info("Продуктов удалено: {}", deletedSize);
        return deletedSize;
    }

    public void removeByUserId(Long userId) {
        logger.info("Удаление продуктов пользователя с id={}", userId);

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var query = session.createQuery("DELETE FROM ProductEntity WHERE creator.id = :userId");
        query.setParameter("userId", userId);

        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        logger.info("Продуктов удалено: {}", deletedSize);
    }
}
