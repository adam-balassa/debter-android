package hu.bme.aut.debter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.RoomActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.home_fragment, container, false);
        configureNewPaymentButton(root);

        return root;
    }

    private void configureNewPaymentButton(View root) {
        FloatingActionButton fab = root.findViewById(R.id.fab);
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(android.R.anim.fade_in)
                .setExitAnim(android.R.anim.fade_out)
                .setPopExitAnim(android.R.anim.fade_out)
                .build();

        fab.setOnClickListener(view -> Navigation.findNavController(root).navigate(R.id.nav_new_payment, new Bundle(), navOptions));
    }

}