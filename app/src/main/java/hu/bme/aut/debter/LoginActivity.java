package hu.bme.aut.debter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.data.APIRoutes;
import hu.bme.aut.debter.data.DebterAPI;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.model.Room;
import hu.bme.aut.debter.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener((view) -> login());

        TextView register = findViewById(R.id.btnRegistrate);
        register.setOnClickListener(view -> redirectToRegistrate());
    }

    private void redirectToRegistrate() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void login() {
        LinearLayout progress = findViewById(R.id.progressbar_view);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        progress.setVisibility(View.VISIBLE);
        final APIRoutes api = DebterAPI.getInstance().getDebter();
        // api.login(new APIRoutes.LoginData(email.getText().toString(), password.getText().toString())).
        api.login(new APIRoutes.LoginData("balassaadi@gmail.com", "password")).
            enqueue( new DebterAPI.DebterCallback<>((call, response) -> {
                User user = response.body().data.getUser();
                UserDataSource.getInstance().setLoggedUser(user);
                api.getUsersRooms(user.getEmail()).enqueue(new DebterAPI.DebterCallback<>((call2, response2) -> {
                    List<APIRoutes.RoomResult> result = response2.body().data;
                    List<Room> rooms = new LinkedList<>();
                    for (APIRoutes.RoomResult room : result)
                        rooms.add(room.getRoom());
                    user.setRooms(rooms);

                    runOnUiThread(() -> progress.setVisibility(View.GONE));
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }));
            }, (call, error) -> {
                runOnUiThread(() -> progress.setVisibility(View.GONE));
                email.setError("Wrong email or password");
            }
        ));
    }

}
