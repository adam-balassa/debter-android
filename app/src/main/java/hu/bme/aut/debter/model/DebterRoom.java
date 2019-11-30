package hu.bme.aut.debter.model;

import java.util.List;

public class DebterRoom extends Room {

    private final List<Member> members;
    private final List<Payment> payments;
    private double rounding;
    private String currency;

    public DebterRoom(String r, String t, List<Member> m, List<Payment> payments, double ro, String c) {
        super(r, t);
        members = m;
        this.payments = payments;
        rounding = ro;
        currency = c;
    }

    public DebterRoom(String r, String t, List<Member> m, List<Payment> payments) {
        this(r, t, m, payments, 1, "HUF");
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public String getCurrency() {
        return currency;
    }

    public double getRounding() {
        return rounding;
    }

    public Member findMemberByName(String name) {
        for (Member member : members)
            if (member.getUser().getName().equals(name))
                return member;
        return null;
    }
}
