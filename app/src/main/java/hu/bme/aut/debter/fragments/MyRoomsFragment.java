package hu.bme.aut.debter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.RoomActivity;
import hu.bme.aut.debter.adapters.RoomListAdapter;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.model.Room;

public class MyRoomsFragment extends Fragment implements RoomListAdapter.RoomClickListener  {

    private RecyclerView recyclerView;
    private RoomListAdapter adapter;
    private UserDataSource userDataSource;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_rooms, container, false);

        FloatingActionButton fab = root.findViewById(R.id.home_fab);
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(android.R.anim.fade_in)
                .setExitAnim(android.R.anim.fade_out)
                .setPopExitAnim(android.R.anim.fade_out)
                .build();

        fab.setOnClickListener(view -> Navigation.findNavController(root).navigate(R.id.nav_new_room, new Bundle(), navOptions));

        this.userDataSource = UserDataSource.getInstance();
        initRecyclerView();

        return root;
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.home_rooms);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutFrozen(true);
        adapter = new RoomListAdapter(userDataSource.getLoggedUser().getRooms(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRoomClicked(Room room) {
        Intent intent = new Intent(getActivity(), RoomActivity.class);
        startActivity(intent);
    }
}
