package hu.bme.aut.debter.fragments;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.RoomActivity;
import hu.bme.aut.debter.data.APIRoutes;
import hu.bme.aut.debter.data.DebterAPI;
import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.model.DebterRoom;

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


        saveButton.setOnClickListener(view -> {
            LinearLayout progress = getActivity().findViewById(R.id.progressbar_view);
            progress.setVisibility(View.VISIBLE);

            String newCurrency = (String) ((Spinner) root.findViewById(R.id.room_currency)).getSelectedItem();
            double newRounding = Double.parseDouble ((String)((Spinner) root.findViewById(R.id.room_rounding)).getSelectedItem());

            APIRoutes api = DebterAPI.getInstance().getDebter();
            final DebterRoom room = RoomDataSource.getInstance().getRoom().getValue();
            api.setNewCurrency(new APIRoutes.Currency(dataSource.getRoom().getValue().getRoomKey(), newCurrency)).enqueue(
                new DebterAPI.DebterCallback<>((call, response) ->
                    api.setNewRounding(new APIRoutes.Rounding(dataSource.getRoom().getValue().getRoomKey(), newRounding)).enqueue(
                    new DebterAPI.DebterCallback<>((call2, response2) ->
                        RoomDataSource.getInstance().loadRoom(room.getRoomKey(),
                        new DebterAPI.DebterCallback<>((call3, response3) -> {
                            getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE));
                            Navigation.findNavController(root).navigateUp();
                        },
                        (__, ___) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE)))),
                    (__, ___) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE)))),
                (__, ___) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE))));
            });
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
        double[] rounding = { 0.01, 0.1, 1, 5, 10, 50, 100, 500 };
        for (int i = 0; i < rounding.length; ++i)
            if (Math.abs(rounding[i] - this.rounding) < 0.001) {
                roundingSelect.setSelection(i);
                break;
            }
    }
}
