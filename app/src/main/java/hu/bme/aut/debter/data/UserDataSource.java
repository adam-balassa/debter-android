package hu.bme.aut.debter.data;

import java.util.List;

import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.User;

public class UserDataSource {
    private User loggedUser;
    private static UserDataSource instance;
    private List<MyDebt> debts;

    private UserDataSource() {

    }

    public static UserDataSource getInstance() {
        if (instance == null)
            instance = new UserDataSource();
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<MyDebt> getDebts() {
        return debts;
    }

    public void setDebts(List<MyDebt> debts) {
        this.debts = debts;
    }
}
