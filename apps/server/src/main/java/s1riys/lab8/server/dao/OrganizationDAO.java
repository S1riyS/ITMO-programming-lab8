package s1riys.lab8.server.dao;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import s1riys.lab8.common.models.Organization;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.entities.OrganizationEntity;
import s1riys.lab8.server.entities.UserEntity;
import s1riys.lab8.server.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class OrganizationDAO {
    private final SessionFactory sessionFactory;
    private final Logger logger = Main.logger;

    public OrganizationDAO() {
        this.sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    }

    public OrganizationEntity add(OrganizationEntity organization) {
        logger.info("Добавление новой организации {}", organization.getName());

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(organization);
        session.getTransaction().commit();
        session.close();

        logger.info("Организация с id={} успешно создана", organization.getId());
        return organization;
    }

    public OrganizationEntity findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        OrganizationEntity organization = session.get(OrganizationEntity.class, id);

        session.getTransaction().commit();
        session.close();

        return organization;
    }

    public OrganizationEntity findByObject(Organization organization) {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        var query = session.createQuery(
                "FROM OrganizationEntity " +
                        "WHERE name = :name " +
                        "AND fullName = :fullName " +
                        "AND annualTurnover = :annualTurnover " +
                        "AND employeesCount = :employeesCount"
        );
        query.setParameter("name", organization.getName());
        query.setParameter("fullName", organization.getFullName());
        query.setParameter("annualTurnover", organization.getAnnualTurnover());
        query.setParameter("employeesCount", organization.getEmployeesCount());

        List<OrganizationEntity> result = (List<OrganizationEntity>) query.list();

        session.getTransaction().commit();
        session.close();

        if (result.isEmpty()) return null;
        return result.get(0);
    }

    public void update(OrganizationEntity updatedOrganization) {
        logger.info("Обновление организации с id={}", updatedOrganization.getId());

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // Retrieving and updating entity
        session.update(updatedOrganization);

        session.getTransaction().commit();
        session.close();

        logger.info("Обновление организации с id={} завершено", updatedOrganization.getId());
    }
}
