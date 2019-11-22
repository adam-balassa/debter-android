package hu.bme.aut.debter.model;

import java.util.List;

public class DebterRoom extends Room {

    final List<Member> members;
    private final List<Payment> payments;

    public DebterRoom(String r, String t, List<Member> m, List<Payment> payments) {
        super(r, t);
        members = m;
        this.payments = payments;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
