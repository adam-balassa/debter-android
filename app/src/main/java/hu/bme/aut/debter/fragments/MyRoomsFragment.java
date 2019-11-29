package hu.bme.aut.debter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import hu.bme.aut.debter.data.APIRoutes;
import hu.bme.aut.debter.data.DebterAPI;
import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.model.DebterRoom;
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

        LinearLayout progress = getActivity().findViewById(R.id.progressbar_home);
        progress.setVisibility(View.VISIBLE);

        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getRoomData(new APIRoutes.RoomKey(room.getRoomKey())).enqueue(
                new DebterAPI.DebterCallback<>((call, response) -> {
                    DebterRoom details = response.body().data.getRoom();
                    api.getFullRoomData(room.getRoomKey()).enqueue(
                            new DebterAPI.DebterCallback<>((call1, response1) -> {
                                DebterRoom debterRoom = response1.body().data.getRoom(details);
                                RoomDataSource.getInstance().getRoom().postValue(debterRoom);

                                getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE));

                                DebterRoom r = RoomDataSource.getInstance().getRoom().getValue();

                                Intent intent = new Intent(getActivity(), RoomActivity.class);
                                startActivity(intent);
                            })
                    );
                    }, (call, throwable) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE))));

    }
}
