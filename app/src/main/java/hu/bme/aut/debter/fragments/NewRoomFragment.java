package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;

import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.data.UsersDataSource;
import hu.bme.aut.debter.model.User;

public class NewRoomFragment extends Fragment {
    View root;
    AutoCompleteTextView userName;
    UsersDataSource dataSource;
    ViewGroup addedUsersView;
    List<User> addedUsers;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_new_room, container, false);

        dataSource = UsersDataSource.getInstance();
        addedUsersView = root.findViewById(R.id.new_room_added_users);

        addedUsers = new LinkedList<>();

        User activeUser = UserDataSource.getInstance().getLoggedUser();
        ((Chip)root.findViewById(R.id.active_user_chip)).setText(activeUser.getName());
        addedUsers.add(activeUser);

        initAddUserSuggestions();
        initAddUserButton();
        initCreateRoomButton();

        return root;
    }

    private void initAddUserButton() {
        Button addUserButton = root.findViewById(R.id.new_room_add_user_button);
        addUserButton.setOnClickListener(view -> {
            String text = userName.getText().toString();
            String email = text.split(" - ")[1];
            User user = dataSource.getUser(email);
            addUser(user);
        });
    }

    private void initAddUserSuggestions() {
        List<User> users = dataSource.getSuggestedUsers(" ");
        List<String> names = new LinkedList<>();
        for (User u : users)
            names.add(u.getName() + " - " + u.getEmail());

        userName = root.findViewById(R.id.new_room_add_user_autocomplete);

        ArrayAdapter a = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, names);
        a.setNotifyOnChange(true);
        userName.setThreshold(1);
        userName.setAdapter(a);
    }

    private void addUser(User user) {
        if (addedUsers.contains(user))
            return;

        addedUsers.add(user);

        LayoutInflater inflater = getLayoutInflater();
        View userView = inflater.inflate(R.layout.compnent_added_user, addedUsersView, false);

        Chip chip = userView.findViewById(R.id.addable_member_chip);
        chip.setText(user.getName());
        chip.setOnCloseIconClickListener(view -> {
            removeChip(userView);
            addedUsers.remove(user);
        });

        addedUsersView.addView(userView);
        userName.setText("");
    }

    private void removeChip(View v) {
        addedUsersView.removeView(v);
    }

    private void initCreateRoomButton() {
        Button createRoomBtn = root.findViewById(R.id.upload_new_room);
        createRoomBtn.setOnClickListener(view -> Navigation.findNavController(root).navigateUp());
    }
}
