package s1riys.lab8.common.models;

import s1riys.lab8.common.models.utils.ModelWithId;
import s1riys.lab8.common.models.utils.Validatable;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a product.
 */
public class Product extends ModelWithId implements Comparable<Product>, Validatable, Serializable {
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Field cannot be null, String cannot be empty
    private Coordinates coordinates; // Field cannot be null
    private Date creationDate; // Field cannot be null, Value of this field should be generated automatically
    private Long price; // Value of the field should be greater than 0
    private String partNumber; // Length of the string should be at least 21, Field can be null
    private UnitOfMeasure unitOfMeasure; // Field can be null
    private Organization manufacturer; // Field can be null

    private Long creatorId;

    /**
     * Constructs a Product object with the specified parameters.
     *
     * @param name          the name of the product (cannot be null or empty)
     * @param coordinates   the coordinates of the product (cannot be null)
     * @param creationDate  the creation date of the product (cannot be null, should be generated automatically)
     * @param price         the price of the product (should be greater than 0)
     * @param partNumber    the part number of the product (length of the string should be at least 21, can be null)
     * @param unitOfMeasure the unit of measure of the product (can be null)
     * @param manufacturer  the manufacturer of the product (can be null)
     */
    public Product(
            Long id,
            String name,
            Coordinates coordinates,
            Date creationDate,
            Long price,
            String partNumber,
            UnitOfMeasure unitOfMeasure,
            Organization manufacturer,
            Long creatorId
    ) {
        super();
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
        this.creatorId = creatorId;
    }

    public Product(
            Long id,
            String name,
            Coordinates coordinates,
            Date creationDate,
            Long price,
            String partNumber,
            UnitOfMeasure unitOfMeasure,
            Organization manufacturer
    ) {
        super();
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
        this.creatorId = (long) -1;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the coordinates of the product.
     *
     * @return the coordinates of the product
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the creation date of the product.
     *
     * @return the creation date of the product
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price of the product
     */
    public Long getPrice() {
        return price;
    }

    /**
     * Returns the part number of the product.
     *
     * @return the part number of the product
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * Returns the unit of measure of the product.
     *
     * @return the unit of measure of the product
     */
    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Returns the manufacturer of the product.
     *
     * @return the manufacturer of the product
     */
    public Organization getManufacturer() {
        return manufacturer;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * Validates the product object.
     *
     * @return true if the product is valid, false otherwise
     */
    @Override
    public boolean validate() {
        if (this.name == null || name.isEmpty()) return false;
        if (this.coordinates == null || !this.coordinates.validate()) return false;
        if (this.creationDate == null) return false;
        if (this.price == null || this.price <= 0) return false;
        if (this.partNumber != null) {
            if (this.partNumber.length() < 21) return false;
        }
        if (this.manufacturer != null) {
            if (!this.manufacturer.validate()) return false;
        }
        return true;
    }

    /**
     * Compares this product with another product based on price and ID.
     *
     * @param o the product to compare to
     * @return a negative integer, zero, or a positive integer as this product is less than, equal to, or greater than the specified product
     */
    @Override
    public int compareTo(Product o) {
        int priceComparison = Long.compare(this.price, o.price);
        if (priceComparison != 0) return priceComparison;
        else return -Long.compare(this.id, o.id);
    }


    /**
     * Returns a string representation of the product.
     *
     * @return a string representation of the product
     */
    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");

        stringJoiner.add("Продукт #%s".formatted(id));
        stringJoiner.add("\tНазвание: %s".formatted(name));
        stringJoiner.add("\tКоординаты: %s".formatted(coordinates));
        stringJoiner.add("\tСоздан: %s".formatted(creationDate));
        stringJoiner.add("\tЦена: %s".formatted(price));
        stringJoiner.add("\tНомер товара: %s".formatted(partNumber));
        stringJoiner.add("\tЕдиницы измерения: %s".formatted(unitOfMeasure));
        stringJoiner.add("\tПроизводитель: %s".formatted(manufacturer));
        stringJoiner.add("\tID владельца: %s".formatted(creatorId));

        return stringJoiner.toString();
    }

    public Product copy(Long id) {
        return new Product(
                id,
                getName(),
                getCoordinates(),
                getCreationDate(),
                getPrice(),
                getPartNumber(),
                getUnitOfMeasure(),
                getManufacturer()
        );
    }

    public Product copy(Long id, Long creatorId) {
        return new Product(
                id,
                getName(),
                getCoordinates(),
                getCreationDate(),
                getPrice(),
                getPartNumber(),
                getUnitOfMeasure(),
                getManufacturer(),
                creatorId
        );
    }

    public void update(Product product) {
        this.name = product.name;
        this.coordinates = product.coordinates;
        this.price = product.price;
        this.partNumber = product.partNumber;
        this.unitOfMeasure = product.unitOfMeasure;
        this.manufacturer = product.manufacturer;
    }

    /**
     * Checks if this product is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the products are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(coordinates, product.coordinates) &&
                Objects.equals(creationDate, product.creationDate) &&
                Objects.equals(price, product.price) &&
                Objects.equals(partNumber, product.partNumber) &&
                unitOfMeasure == product.unitOfMeasure &&
                Objects.equals(manufacturer, product.manufacturer);
    }
}
