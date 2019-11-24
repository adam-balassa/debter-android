package hu.bme.aut.debter.data;

import hu.bme.aut.debter.model.User;

public class UserDataSource {
    private final User loggedUser;
    private static UserDataSource instance;

    private UserDataSource() {
        loggedUser = LoginDataSource.getInstance().getUser("balassaadi@gmail.com");
    }

    public static UserDataSource getInstance() {
        if (instance == null)
            instance = new UserDataSource();
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
