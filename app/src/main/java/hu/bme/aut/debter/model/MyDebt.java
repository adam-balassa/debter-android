package hu.bme.aut.debter.model;

public class MyDebt {
    private final Member to;
    private final String currency;
    private final Double value;
    private final String roomName;
    private final String roomKey;
    private final String fromMember;
    public MyDebt(Member to, String currency, Double value, String roomName, String roomKey, String fromId) {
        this.to = to;
        this.currency = currency;
        this.value = value;
        this.roomName = roomName;
        this.roomKey = roomKey;
        this.fromMember = fromId;
    }

    public Member getTo() {
        return to;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getValue() {
        return value;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getFromMember() {
        return fromMember;
    }

    public String getRoomKey() {
        return roomKey;
    }

}
