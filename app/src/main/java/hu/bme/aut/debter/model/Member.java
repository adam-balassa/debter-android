package hu.bme.aut.debter.model;

import java.util.List;

public class Member {
    final String id;
    final User user;
    final List<Debt> debts;

    public Member (String id, User user, List<Debt> debts) {
        this.id = id;
        this.user = user;
        this.debts = debts;
    }

    public String getId() {
        return id;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public User getUser() {
        return user;
    }
}
