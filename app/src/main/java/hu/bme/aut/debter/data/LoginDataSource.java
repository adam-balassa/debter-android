package hu.bme.aut.debter.data;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.bme.aut.debter.model.User;

public class LoginDataSource {
    private MutableLiveData<List<User>> users;

    private static LoginDataSource instance;

    public static LoginDataSource getInstance() {
        if (instance == null)
            instance = new LoginDataSource();
        return instance;
    }

    private LoginDataSource() {
        User[] u = {
                new User("Ádám Balassa", "balassaadi@gmail.com", "password"),
                new User("Béla Kovács", "bela.kovacs@gmail.com", "password"),
                new User("Péter Kiss", "kiss.peter@gmail.com", "password"),
                new User("Teodóra Faragó", "farago.teodora@gmail.com", "password")
        };
        users = new MutableLiveData<>();
        List<User> userList = new ArrayList<>(4);
        userList.addAll(Arrays.asList(u));
        users.setValue(userList);
    }

    User getUser(String email) {
        for (User user: users.getValue())
            if (user.getEmail() == email)
                return user;
        return null;
    }
}
