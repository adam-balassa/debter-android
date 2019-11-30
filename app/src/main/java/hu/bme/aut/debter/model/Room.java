package hu.bme.aut.debter.model;

public class Room {
    private final String id;
    private final String roomKey;
    private final String title;

    public Room (String i, String r, String t) {
        id = i;
        roomKey = r;
        title = t;
    }

    public Room(String r, String t) {
        this(null, r, t);
    }

    public String getId() {
        return id;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public String getTitle() {
        return title;
    }
}
