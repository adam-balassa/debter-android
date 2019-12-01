package hu.bme.aut.debter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.data.services.UserDataSource;

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

        UserDataSource.getInstance().login(email.getText().toString(), password.getText().toString(),
                new DebterAPI.DebterCallback<>((call, response) -> {
                    runOnUiThread(() -> progress.setVisibility(View.GONE));
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }, (call, throwable)-> {
                    runOnUiThread(() -> progress.setVisibility(View.GONE));
                    email.setError("Wrong email or password");
                }));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();
            if ( v instanceof EditText) {


                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
