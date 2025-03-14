package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.models.User;

import java.io.Serializable;
import java.util.Objects;

public abstract class Request implements Serializable {
    private final String name;
    private final User user;

    public Request(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request response = (Request) o;
        return Objects.equals(name, response.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                '}';
    }
}