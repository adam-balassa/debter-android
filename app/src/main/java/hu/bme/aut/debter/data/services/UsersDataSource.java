package hu.bme.aut.debter.data.services;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.data.api.APIRoutes;
import hu.bme.aut.debter.data.api.DebterAPI;
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
        users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());
        loadUsers();
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

    public void loadUsers() {
        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getUsers().enqueue(new DebterAPI.DebterCallback<>((call, response) -> {
            List<User> users = new LinkedList<>();
            for (APIRoutes.UserResult user : response.body().data)
                users.add(user.getUser());
            this.users.postValue(users);
        }));
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }
}
