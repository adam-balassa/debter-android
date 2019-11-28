package hu.bme.aut.debter.data;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.DebterRoom;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.Payment;
import hu.bme.aut.debter.model.User;

public class RoomDataSource {
    MutableLiveData<DebterRoom> room;
    private static RoomDataSource instance;

    public static RoomDataSource getInstance() {
        if (instance == null)
            instance = new RoomDataSource();
        return instance;
    }

    private RoomDataSource () {
        User[] u = {
                UserDataSource.getInstance().getLoggedUser(),
                new User("Béla Kovács", "bela.kovacs@gmail.com", null),
                new User("Péter Kiss", "kiss.peter@gmail.com", null),
                new User("Teodóra Faragó", "farago.teodora@gmail.com", null)
        };
        List<User> users = new ArrayList<>(4);
        List<Member> members = new ArrayList<>(4);

        for (User user : u) {
            users.add(user);
            members.add(new Member("", user, new ArrayList<>(2)));
        }

        Debt[] debtBela = {
                new Debt(members.get(1), members.get(2), 4500, "HUF", true),
                new Debt(members.get(1), members.get(0), 1200, "HUF", false),
                new Debt(members.get(1), members.get(3), 4500, "HUF", true),
        };
        Debt[] debtAdam = {
                new Debt(members.get(2), members.get(0), 3450, "HUF", false),
        };

        members.get(1).getDebts().add(debtBela[0]);
        members.get(1).getDebts().add(debtBela[1]);
        members.get(1).getDebts().add(debtBela[2]);
        members.get(0).getDebts().add(debtAdam[0]);

        List<Payment> payments = new LinkedList<>();
        payments.add(new Payment(members.get(0), 9000, "HUF", new Date(2018 - 1900, 6, 28), "buszjegyek", members.subList(2, 3)));
        payments.add(new Payment(members.get(0), 12000, "HUF", new Date(2018 - 1900, 6, 28), "bevásárlás az aldiban", members));
        payments.add(new Payment(members.get(0), 4560, "HUF", new Date(2018 - 1900, 6, 29), "hazaút", members.subList(0, 3)));
        payments.add(new Payment(members.get(1), 1200, "HUF", new Date(2018 - 1900, 6, 28), "hotdogok", members));
        payments.add(new Payment(members.get(1), 1890, "HUF", new Date(2018 - 1900, 7, 1), "képeslapok", members.subList(0, 1)));
        payments.add(new Payment(members.get(2), 2130, "HUF", new Date(2019 - 1900, 10, 20), "sör", members.subList(1, 3)));
        payments.add(new Payment(members.get(2), 1600, "HUF", new Date(2018 - 1900, 6, 30), "benzin az autópálya mellett", members));
        payments.add(new Payment(members.get(3), 250, "EUR", new Date(2018 - 1900, 6, 28), "síbérletek", members));
        payments.add(new Payment(members.get(3), 1100, "HUF", new Date(), "borravaló", members));
        payments.add(new Payment(members.get(3), 600, "HUF", new Date(2019 - 1900, 1, 21), "bélyegek", members));
        payments.add(new Payment(members.get(3), 8100, "HUF", new Date(2019 - 1900, 1, 20), "pótdíj", members));

        room = new MutableLiveData<>();
        room.setValue(new DebterRoom("CHOPOK", "Síelés Chopokon", members, payments));
    }

    public MutableLiveData<DebterRoom> getRoom() {
        return room;
    }
}
