package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.data.RoomDataSource;

public class SettingsFragment extends Fragment {

    double rounding;
    String currency;
    RoomDataSource dataSource;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_room_settings, container, false);

        dataSource = RoomDataSource.getInstance();

        initRounding();
        initCurrency();
        initSaveButton();

        return root;
    }

    private void initSaveButton() {
        Button saveButton = root.findViewById(R.id.save_room_settings);
        saveButton.setOnClickListener(view -> Navigation.findNavController(root).navigateUp());
    }

    private void initCurrency() {
        currency = dataSource.getRoom().getValue().getCurrency();
        Spinner currencySelect = root.findViewById(R.id.room_currency);
        String[] currencies = { "HUF", "EUR", "USD" };
        for (int i = 0; i < currencies.length; ++i)
            if (currencies[i].equals(currency)) {
                currencySelect.setSelection(i);
                break;
            }

    }

    private void initRounding() {
        rounding = dataSource.getRoom().getValue().getRounding();
        Spinner roundingSelect = root.findViewById(R.id.room_rounding);
        double[] rounding = { 0.01, 0.1, 1, 5, 10, 5, 100, 500 };
        for (int i = 0; i < rounding.length; ++i)
            if (rounding[i] == this.rounding) {
                roundingSelect.setSelection(i);
                break;
            }
    }
}
