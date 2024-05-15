package s1riys.lab8.client.forms;

import s1riys.lab8.common.exceptions.InvalidFormException;

/**
 * Interface for forms that build an object of type T.
 *
 * @param <T> the type of object to build
 * @author s1riys
 */
public interface Form<T> {
    public T build() throws InvalidFormException;
}
