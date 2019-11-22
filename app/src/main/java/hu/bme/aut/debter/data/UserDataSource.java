package hu.bme.aut.debter.data;

import hu.bme.aut.debter.model.User;

public class UserDataSource {
    final User loggedUser;

    public UserDataSource() {
        loggedUser = LoginDataSource.getInstance().getUser("balassaadi@gmail.com");
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
