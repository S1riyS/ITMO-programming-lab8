package s1riys.lab8.server.entities;

import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.UnitOfMeasure;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(name = "name", nullable = false)
    private String name; // Field cannot be null, String cannot be empty

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private Long y;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate; // Field cannot be null, Value of this field should be generated automatically

    @Column(name = "price", nullable = false)
    private Long price; // Value of the field should be greater than 0

    @Column(name = "part_number")
    private String partNumber; // Length of the string should be at least 21, Field can be null

    @Column(name = "unit_of_measure")
    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure; // Field can be null

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private OrganizationEntity manufacturer; // Field can be null

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    public ProductEntity() {
    }

    public ProductEntity(Product product) {
        this.name = product.getName();
        this.x = product.getCoordinates().getX();
        this.y = product.getCoordinates().getY();
        this.creationDate = product.getCreationDate();
        this.price = product.getPrice();
        this.partNumber = product.getPartNumber();
        this.unitOfMeasure = product.getUnitOfMeasure();
    }

    public void update(Product product) {
        this.name = product.getName();
        this.x = product.getCoordinates().getX();
        this.y = product.getCoordinates().getY();
        this.creationDate = product.getCreationDate();
        this.price = product.getPrice();
        this.partNumber = product.getPartNumber();
        this.unitOfMeasure = product.getUnitOfMeasure();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public OrganizationEntity getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(OrganizationEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }
}
