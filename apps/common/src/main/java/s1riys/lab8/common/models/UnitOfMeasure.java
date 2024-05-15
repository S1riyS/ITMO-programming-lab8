package s1riys.lab8.common.models;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Represents units of measure.
 */
public enum UnitOfMeasure implements Serializable {
    METERS,
    PCS,
    LITERS,
    GRAMS,
    MILLIGRAMS;

    /**
     * Returns a comma-separated string of all unit names.
     *
     * @return a string containing all unit names
     */
    public static String getNames() {
        StringJoiner joiner = new StringJoiner(", ");
        for (UnitOfMeasure unitOfMeasure : UnitOfMeasure.values()) {
            joiner.add(unitOfMeasure.name());
        }
        return joiner.toString();
    }
}
