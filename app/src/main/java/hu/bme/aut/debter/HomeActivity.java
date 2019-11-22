package hu.bme.aut.debter;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hu.bme.aut.debter.adapters.RoomListAdapter;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.model.Room;

public class HomeActivity extends AppCompatActivity implements RoomListAdapter.RoomClickListener {

    private RecyclerView recyclerView;
    private RoomListAdapter adapter;
    private UserDataSource userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        this.userDataSource = new UserDataSource();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.home_rooms);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutFrozen(true);
        adapter = new RoomListAdapter(userDataSource.getLoggedUser().getRooms(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRoomClicked(Room room) {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }
}
