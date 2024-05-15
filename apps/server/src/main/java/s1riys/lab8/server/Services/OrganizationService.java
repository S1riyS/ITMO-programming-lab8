package s1riys.lab8.server.Services;

import s1riys.lab8.common.models.Organization;
import s1riys.lab8.server.dao.OrganizationDAO;
import s1riys.lab8.server.entities.OrganizationEntity;

public class OrganizationService implements Service {
    private final OrganizationDAO organizationDAO;

    public OrganizationService() {
        this.organizationDAO = new OrganizationDAO();
    }

    public OrganizationEntity add(Organization organization) {
        OrganizationEntity entity = new OrganizationEntity(organization);
        return organizationDAO.add(entity);
    }

    public OrganizationEntity findById(Long id) {
        return organizationDAO.findById(id);
    }

    public OrganizationEntity findByObject(Organization organizationObject) {
        return organizationDAO.findByObject(organizationObject);
    }

    public void update(Long id, Organization updatedOrganization) {
        OrganizationEntity entityToUpdate = organizationDAO.findById(id);
        entityToUpdate.update(updatedOrganization);
        organizationDAO.update(entityToUpdate);
    }

    @Override
    public String getName() {
        return "OrganizationService";
    }
}
