package hu.bme.aut.debter.data.services;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import hu.bme.aut.debter.data.api.APIRoutes;
import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.Room;
import hu.bme.aut.debter.model.User;
import retrofit2.Callback;

public class UserDataSource {
    private DatabaseService db;
    private User loggedUser;
    private static UserDataSource instance;
    private List<MyDebt> debts;
    private List<Room> rooms;

    private UserDataSource() {
        debts = new LinkedList<>();
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void login(String email, String password, Callback<APIRoutes.Response<ArrayList<APIRoutes.RoomResult>>> callback) {
        final APIRoutes api = DebterAPI.getInstance().getDebter();
        // api.login(new APIRoutes.LoginData(email.getText().toString(), password.getText().toString())).
        api.login(new APIRoutes.LoginData("balassaadi@gmail.com", "password"))
                .enqueue(new DebterAPI.DebterCallback<>((call, response) -> {
                    User user = response.body().data.getUser();
                    setLoggedUser(user);
                    db.open();
                    db.saveLoggedUser(user);
                    db.close();
                    loadUsersRooms("balassaadi@gmail.com", callback);
                }));
    }

    public void loadUsersRooms(String email, Callback<APIRoutes.Response<ArrayList<APIRoutes.RoomResult>>> callback) {
        final APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getUsersRooms(email).enqueue(new DebterAPI.DebterCallback<>((call, response) -> {
            List<APIRoutes.RoomResult> result = response.body().data;
            List<Room> rooms = new LinkedList<>();
            for (APIRoutes.RoomResult room : result)
                rooms.add(room.getRoom());
            setRooms(rooms);
            callback.onResponse(call, response);
        }, callback::onFailure));
    }

    public void setDb(DatabaseService db) {
        this.db = db;
    }
}
