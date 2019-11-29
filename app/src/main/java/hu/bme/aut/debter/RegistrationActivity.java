package hu.bme.aut.debter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hu.bme.aut.debter.data.APIRoutes;
import hu.bme.aut.debter.data.DebterAPI;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initRegisterButton();
    }

    private void initRegisterButton() {
        TextView registrate = findViewById(R.id.registration_button);
        registrate.setOnClickListener(view -> {

            APIRoutes api = DebterAPI.getInstance().getDebter();
            EditText email = findViewById(R.id.registration_email);
            EditText password = findViewById(R.id.registration_password);
            EditText password2 = findViewById(R.id.registration_password2);
            EditText name = findViewById(R.id.registration_name);

            String password1 = password.getText().toString(), password02 = password2.getText().toString();
            if (password.length() < 8){
                password.setError("Password is too weak");
                return;
            }
            if (!password1.equals(password02)) {
                password.setError("Two passwords don't match");
                return;
            }
            LinearLayout progress = findViewById(R.id.progressbar_view);
            progress.setVisibility(View.VISIBLE);
            String firstName = name.getText().toString().split(" ")[0];
            String lastName = name.getText().toString().split(" ")[1];
            api.register(new APIRoutes.RegisterData(email.getText().toString(), password1, firstName, lastName))
                .enqueue( new DebterAPI.DebterCallback<APIRoutes.Response<Object>>((call, response) -> {
                    runOnUiThread(() -> progress.setVisibility(View.GONE));
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }, (call, error) -> runOnUiThread(() -> progress.setVisibility(View.GONE))));
        });
    }
}
