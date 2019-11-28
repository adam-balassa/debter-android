package hu.bme.aut.debter.model;

public class MyDebt {
    private final Member to;
    private final String currency;
    private final Double value;
    private final String roomName;
    public MyDebt(Member to, String currency, Double value, String roomName) {
        this.to = to;
        this.currency = currency;
        this.value = value;
        this.roomName = roomName;
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
}
