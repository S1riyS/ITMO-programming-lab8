package s1riys.lab8.common.models;

import s1riys.lab8.common.models.utils.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the coordinates of a location.
 */
public class Coordinates implements Validatable, Serializable {
    private Integer x; // Значение поля должно быть больше -205
    private Long y; // Значение поля должно быть больше -558, Поле не может быть null

    /**
     * Constructs a Coordinates object with the specified x and y values.
     *
     * @param x the x coordinate value
     * @param y the y coordinate value
     */
    public Coordinates(Integer x, Long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate value.
     *
     * @return the x coordinate value
     */
    public Integer getX() {
        return x;
    }

    /**
     * Returns the y coordinate value.
     *
     * @return the y coordinate value
     */
    public Long getY() {
        return y;
    }

    /**
     * Validates the Coordinates object.
     *
     * @return true if the coordinates are valid, false otherwise
     */
    @Override
    public boolean validate() {
        if (this.x == null || this.x <= -205) return false;
        if (this.y == null || this.y <= -558) return false;
        return true;
    }

    /**
     * Returns a string representation of the Coordinates object.
     *
     * @return a string representation of the Coordinates object
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x = " + this.x + ", " +
                "y = " + this.y +
                "}";
    }

    /**
     * Checks if the specified object is equal to the Coordinates object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return Objects.equals(x, coordinates.x) && Objects.equals(y, coordinates.y);
    }
}
