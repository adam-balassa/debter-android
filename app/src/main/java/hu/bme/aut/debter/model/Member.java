package hu.bme.aut.debter.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    final String id;
    final User user;
    final List<Debt> debts;

    public Member(String id, String name) {
        this(id, name, new ArrayList<>());
    }

    public Member (String id, User user, List<Debt> debts) {
        this.id = id;
        this.user = user;
        this.debts = debts;
    }

    public Member (String id, String name, List<Debt> debts) {
        this.id = id;
        this.debts = debts;
        this.user = new User(name, "", new ArrayList<>());
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
