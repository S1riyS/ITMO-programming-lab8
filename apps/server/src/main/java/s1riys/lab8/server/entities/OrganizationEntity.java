package s1riys.lab8.server.entities;

import s1riys.lab8.common.models.Organization;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
public class OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(name = "name", nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(name = "full_name")
    private String fullName; //Длина строки не должна быть больше 1336, Поле может быть null

    @Column(name = "annual_turnover")
    private Double annualTurnover; //Поле может быть null, Значение поля должно быть больше 0

    @Column(name = "employees_count", nullable = false)
    private Integer employeesCount; //Значение поля должно быть больше 0

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private List<ProductEntity> products = new ArrayList<>();

    public OrganizationEntity() {
    }

    public OrganizationEntity(Organization organization) {
        this.name = organization.getName();
        this.fullName = organization.getFullName();
        this.annualTurnover = organization.getAnnualTurnover();
        this.employeesCount = organization.getEmployeesCount();
    }

    public void update(Organization organization) {
        this.name = organization.getName();
        this.fullName = organization.getFullName();
        this.annualTurnover = organization.getAnnualTurnover();
        this.employeesCount = organization.getEmployeesCount();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public int getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
