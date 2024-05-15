package s1riys.lab8.common.models;

import s1riys.lab8.common.models.utils.ModelWithId;
import s1riys.lab8.common.models.utils.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an organization.
 */
public class Organization extends ModelWithId implements Validatable, Serializable {
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String fullName; //Длина строки не должна быть больше 1336, Поле может быть null
    private Double annualTurnover; //Поле может быть null, Значение поля должно быть больше 0
    private Integer employeesCount; //Значение поля должно быть больше 0

    /**
     * Constructs an Organization object with the specified parameters.
     *
     * @param name           the name of the organization (cannot be null or empty)
     * @param fullName       the full name of the organization (can be null)
     * @param annualTurnover the annual turnover of the organization (can be null, must be greater than 0)
     * @param employeesCount the number of employees in the organization (must be greater than 0)
     */
    public Organization(Long id, String name, String fullName, Double annualTurnover, Integer employeesCount) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
    }

    public Organization() {
    }

    /**
     * Returns the name of the organization.
     *
     * @return the name of the organization
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the full name of the organization.
     *
     * @return the full name of the organization
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the annual turnover of the organization.
     *
     * @return the annual turnover of the organization
     */
    public Double getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Returns the number of employees in the organization.
     *
     * @return the number of employees in the organization
     */
    public Integer getEmployeesCount() {
        return employeesCount;
    }

    /**
     * Validates the organization object.
     *
     * @return true if the organization is valid, false otherwise
     */
    @Override
    public boolean validate() {
        if (this.name == null || name.isEmpty()) return false;
        if (this.fullName == null || fullName.length() > 1366) return false;
        if (this.annualTurnover == null || annualTurnover <= 0) return false;
        if (this.employeesCount == null || this.employeesCount <= 0) return false;
        return true;
    }

    /**
     * Returns a string representation of the organization.
     *
     * @return a string representation of the organization
     */
    @Override
    public String toString() {
        return "Organisation{" +
                "id = " + this.id + ", " +
                "name = " + this.name + ", " +
                "fullName = " + this.fullName + ", " +
                "annualTurnover = " + this.annualTurnover + ", " +
                "employeesCount = " + this.employeesCount +
                "}";
    }

    /**
     * Checks if this organization is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the organizations are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization organization = (Organization) o;
        return Objects.equals(employeesCount, organization.employeesCount) &&
                Objects.equals(name, organization.name) &&
                Objects.equals(fullName, organization.fullName) &&
                Objects.equals(annualTurnover, organization.annualTurnover);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
