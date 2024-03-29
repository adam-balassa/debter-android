package hu.bme.aut.debter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.data.api.APIRoutes;
import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.data.services.DatabaseService;
import hu.bme.aut.debter.data.services.UserDataSource;
import hu.bme.aut.debter.model.MyDebt;

public class HomeActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        configureNavigationBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserDebts();
    }

    private void loadUserDebts() {
        final UserDataSource data = UserDataSource.getInstance();
        final APIRoutes api = DebterAPI.getInstance().getDebter();
        api.getUsersDebts(data.getLoggedUser().getEmail()).
            enqueue( new DebterAPI.DebterCallback<>((call, response) -> {

                List<APIRoutes.DebtResult> result = response.body().data;
                List<MyDebt> debts = new LinkedList<>();
                for (APIRoutes.DebtResult debt : result)
                    debts.add(debt.getMyDebt());
                data.setDebts(debts);
            }
        ));
    }

    private void initNavigationBar(View navHeader) {
        UserDataSource data = UserDataSource.getInstance();

        TextView name = navHeader.findViewById(R.id.nav_home_name);
        TextView email = navHeader.findViewById(R.id.nav_home_email);

        name.setText(data.getLoggedUser().getName());
        email.setText(data.getLoggedUser().getEmail());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void configureNavigationBar() {
        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        NavigationView navigationView = findViewById(R.id.home_nav_view);
        initNavigationBar(navigationView.getHeaderView(0));
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_my_rooms, R.id.nav_my_debts, R.id.nav_settings, R.id.nav_bank_accounts)
                .setDrawerLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_home);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public static class Logout extends Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            DatabaseService db = new DatabaseService(getContext());
            db.open();
            db.logout();
            db.close();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }
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
