package s1riys.lab8.common.models;

import s1riys.lab8.common.models.utils.Validatable;

import java.io.Serializable;

public class User implements Serializable, Validatable {
    private final Long id;
    private final String name;
    private final String password;

    public User(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public boolean validate() {
        return getName().length() < 40;
    }

    public User copy(Long id) {
        return new User(id, getName(), getPassword());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='********'" +
                '}';
    }

    public Integer compareTo(User element) {
        return (int) (this.getId() - element.getId());
    }
}
