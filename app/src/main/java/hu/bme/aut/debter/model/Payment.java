package hu.bme.aut.debter.model;

import java.util.Date;
import java.util.List;

public class Payment {
    private final String id;
    private final Member member;
    private final double value;
    private final String currency;
    private final double realValue;
    private final Date date;
    private final boolean active;
    private final String note;
    private final List<Member> included;

    public Payment (String i, Member m, double v, String c, double r, Date d, String n, boolean a, List<Member> in) {
        id = i;
        member = m;
        value = v;
        currency = c;
        realValue = r;
        date = d;
        note = n;
        active = a;
        included = in;
    }

    public Payment (Member m, double v, String c, Date d, String note, List<Member> in) {
        this (null, m, v, c, v, d, note, true, in);
    }

    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }

    public double getRealValue() {
        return realValue;
    }

    public double getValue() {
        return value;
    }

    public List<Member> getIncluded() {
        return included;
    }

    public String getNote() {
        return note;
    }

    public Member getMember() {
        return member;
    }
}
