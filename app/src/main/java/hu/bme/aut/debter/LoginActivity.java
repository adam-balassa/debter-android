package hu.bme.aut.debter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener((view) -> {
            redirectToHome();
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
