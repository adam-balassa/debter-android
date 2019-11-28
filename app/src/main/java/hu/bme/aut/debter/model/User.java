package hu.bme.aut.debter.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final String name;
    private final String email;

    private List<Room> rooms;

    public User(String n, String e, List<Room> rooms) {
        name = n;
        email = e;
        this.rooms = rooms;

    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}

