package hu.bme.aut.debter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.data.services.DatabaseService;
import hu.bme.aut.debter.data.services.UserDataSource;
import hu.bme.aut.debter.model.User;

public class MainActivity extends AppCompatActivity {

    UserDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findLoggedUserOrLogin();
    }

    private void findLoggedUserOrLogin() {
        DatabaseService db = new DatabaseService(this);
        db.open();
        dataSource = UserDataSource.getInstance();
        dataSource.setDb(db);
        User loggedUser = db.getLoggedUser();
        db.close();
        if (loggedUser == null)
            redirectToLogin();
        else
            continueToHome(loggedUser);
    }

    private void redirectToLogin() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });
        t.start();
    }

    private void continueToHome(User user) {
        dataSource.setLoggedUser(user);
        dataSource.loadUsersRooms(user.getEmail(), new DebterAPI.DebterCallback<>(((call, response) -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        })));
    }

}
