package hu.bme.aut.debter.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final String name;
    private final String email;

    public User(String n, String e) {
        name = n;
        email = e;

    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}

