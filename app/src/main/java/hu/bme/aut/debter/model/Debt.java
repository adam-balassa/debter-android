package hu.bme.aut.debter.model;

public class Debt {
    private final Member from;
    private final Member to;
    private final double value;
    private final String currency;
    private boolean arranged;

    public Debt (Member f, Member t, double v, String c, boolean a) {
        from = f;
        to = t;
        value = v;
        currency = c;
        arranged = a;
    }

    public double getValue() {
        return value;
    }

    public Member getFrom() {
        return from;
    }

    public Member getTo() {
        return to;
    }

    public String getCurrency() {
        return currency;
    }

}
