package s1riys.lab8.client.utils;

import s1riys.lab8.common.models.Organization;
import s1riys.lab8.common.models.Product;

public class ProductRenderer {
    private final Localizator localizator;

    public ProductRenderer(Localizator localizator) {
        this.localizator = localizator;
    }

    public String describe(Product product) {
        String info = "";
        info += " ID: " + product.getId();
        info += "\n " + localizator.getKeyString("Name") + ": " + product.getName();
        info += "\n " + localizator.getKeyString("OwnerId") + ": " + product.getCreatorId();
        info += "\n " + localizator.getKeyString("CreationDate") + ": " + localizator.getDate(product.getCreationDate());
        info += "\n X: " + product.getCoordinates().getX();
        info += "\n Y: " + product.getCoordinates().getY();
        info += "\n " + localizator.getKeyString("Price") + ": " + product.getPrice();
        info += "\n " + localizator.getKeyString("PartNumber") + ": " + product.getPartNumber();
        info += "\n " + localizator.getKeyString("UnitOfMeasure") + ": " + product.getUnitOfMeasure();

        info += "\n " + localizator.getKeyString("Manufacturer") + describeManufacturer(product.getManufacturer());

        return info;
    }

    public String describeManufacturer(Organization organization) {
        if (organization == null) return ": null";

        String info = "";
        info += "\n    " + localizator.getKeyString("ManufacturerId") + ": " + organization.getId();
        info += "\n    " + localizator.getKeyString("ManufacturerName") + ": " + organization.getName();
        info += "\n    " + localizator.getKeyString("ManufacturerFullName") + ": " + organization.getFullName();
        info += "\n    " + localizator.getKeyString("ManufacturerAnnualTurnover") + ": " + localizator.getNumber(organization.getAnnualTurnover());
        info += "\n    " + localizator.getKeyString("ManufacturerEmployeesCount") + ": " + organization.getEmployeesCount();

        return info;
    }
}
