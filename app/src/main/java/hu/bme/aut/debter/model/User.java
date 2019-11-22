package hu.bme.aut.debter.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final List<Room> rooms;

    public User(String i, String n, String e, String p) {
        id = i;
        name = n;
        email = e;
        password = p;

        rooms = new ArrayList<>();
        rooms.add (new Room("CHOPOK", "Síelés Chopokon"));
        rooms.add (new Room("BERLIN", "Berlin"));
        rooms.add (new Room("WQFUOZ", "Balatonozás Benénél"));
        rooms.add (new Room("DXXEXO", "Evezés 2019"));
        rooms.add (new Room("VIKJQP", "Hangya"));
    }

    public User(String n, String e, String p) {
        this(null, n, e, p);
    }

    public String getId() {
        return id;
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

    public String getPassword() {
        return password;
    }
}

