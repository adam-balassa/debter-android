package hu.bme.aut.debter.data;

import java.util.Arrays;
import java.util.List;

import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.User;

public class UserDataSource {
    private final User loggedUser;
    private static UserDataSource instance;

    private UserDataSource() {
        loggedUser = UsersDataSource.getInstance().getUser("balassaadi@gmail.com");
    }

    public static UserDataSource getInstance() {
        if (instance == null)
            instance = new UserDataSource();
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public List<Debt> getDebts() {
        return RoomDataSource.getInstance().getRoom().getValue().getMembers().get(1).getDebts();
    }
}
