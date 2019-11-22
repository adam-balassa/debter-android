package hu.bme.aut.debter.ui.debts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.ui.history.HistoryViewModel;

public class DebtsFragment extends Fragment {
    private DebtsViewModel debtsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        debtsViewModel = ViewModelProviders.of(this).get(DebtsViewModel.class);
        View root = inflater.inflate(R.layout.debts_fragment, container, false);

        final TextView textView = root.findViewById(R.id.text_debts);
        debtsViewModel.getText().observe(this, textView::setText);

        return root;
    }
}
