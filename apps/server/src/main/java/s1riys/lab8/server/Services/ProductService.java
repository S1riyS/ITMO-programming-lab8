package s1riys.lab8.server.Services;

import s1riys.lab8.common.models.Organization;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.User;
import s1riys.lab8.server.Services.utils.ServiceLocator;
import s1riys.lab8.server.dao.ProductDAO;
import s1riys.lab8.server.entities.OrganizationEntity;
import s1riys.lab8.server.entities.ProductEntity;
import s1riys.lab8.server.entities.UserEntity;

import java.util.List;

public class ProductService implements Service {
    private final ProductDAO productDAO;
    private final UserService userService = (UserService) ServiceLocator.getService("UserService");
    private final OrganizationService organizationService = (OrganizationService) ServiceLocator.getService("OrganizationService");

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public Long add(Product product, User user) {
        ProductEntity productEntity = new ProductEntity(product);

        UserEntity owner = userService.findById(user.getId());
        productEntity.setCreator(owner);

        OrganizationEntity manufacturer = null;
        if (product.getManufacturer() != null) {
            OrganizationEntity existingOrganization = organizationService.findByObject(product.getManufacturer());

            if (existingOrganization == null) manufacturer = organizationService.add(product.getManufacturer());
            else manufacturer = existingOrganization;
        }
        productEntity.setManufacturer(manufacturer);

        Long newId = productDAO.add(productEntity);
        return newId;
    }

    public ProductEntity findById(Long id) {
        return productDAO.findById(id);
    }

    public List<ProductEntity> findAll() {
        return productDAO.findAll();
    }

    public void update(Long id, Product updatedProduct) {
        ProductEntity entityToUpdate = productDAO.findById(id);

        if (updatedProduct.getManufacturer() == null) entityToUpdate.setManufacturer(null);
        else {
            OrganizationEntity existingOrganization = organizationService.findByObject(updatedProduct.getManufacturer());
            if (existingOrganization != null) entityToUpdate.setManufacturer(existingOrganization);
            else {
                Organization updatedManufacturer = updatedProduct.getManufacturer();
                OrganizationEntity newOrganization = organizationService.add(updatedManufacturer);
                entityToUpdate.setManufacturer(newOrganization);
            }
        }
        entityToUpdate.update(updatedProduct);
        productDAO.update(entityToUpdate);
    }

    public int removeById(Long id) {
        return productDAO.removeById(id);
    }

    public void clear(Long userId) {
        productDAO.removeByUserId(userId);
    }

    @Override
    public String getName() {
        return "ProductService";
    }
}
