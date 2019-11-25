package hu.bme.aut.debter.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.model.User;

public class UsersDataSource {
    private MutableLiveData<List<User>> users;

    private static UsersDataSource instance;

    public static UsersDataSource getInstance() {
        if (instance == null)
            instance = new UsersDataSource();
        return instance;
    }

    private UsersDataSource() {
        User[] u = {
                new User("Ádám Balassa", "balassaadi@gmail.com", "password"),
                new User("Béla Kovács", "bela.kovacs@gmail.com", "password"),
                new User("Péter Kiss", "kiss.peter@gmail.com", "password"),
                new User("Teodóra Faragó", "farago.teodora@gmail.com", "password"),
                new User("Kalapos József", "jozsef.kalapos@gmail.com", "password"),
                new User("Kozma Kata", "kata.kozma@gmail.com", "password"),
                new User("Bárányos Ferenc", "bferi@gmail.com", "password"),
                new User("Kolozsvári Gergő", "kolger98@gmail.com", "password")
        };
        users = new MutableLiveData<>();
        List<User> userList = new ArrayList<>(8);
        userList.addAll(Arrays.asList(u));
        users.setValue(userList);
    }

    public List<User> getSuggestedUsers (String str){
        List<User> u =  this.users.getValue();
        List<User> suggestions = new LinkedList<>();
        for (User user : u)
            if (user.getEmail().contains(str) || user.getName().contains(str))
                suggestions.add(user);

        return suggestions;
    }

    public User getUser(String email) {

        for (User user: users.getValue())
            if (user.getEmail().equals(email))
                return user;
        return null;
    }
}
