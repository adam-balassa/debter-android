package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hu.bme.aut.debter.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
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