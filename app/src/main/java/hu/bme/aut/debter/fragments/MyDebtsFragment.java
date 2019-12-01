package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.adapters.MyDebtsAdapter;
import hu.bme.aut.debter.data.api.APIRoutes;
import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.data.services.RoomDataSource;
import hu.bme.aut.debter.data.services.UserDataSource;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.MyDebt;

public class MyDebtsFragment extends Fragment implements MyDebtsAdapter.MyDebtOnClickListener {
    UserDataSource dataSource;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_my_debts, container, false);
        this.dataSource = UserDataSource.getInstance();

        initializeMyDebtRecyclerView();
        return root;
    }

    private void initializeMyDebtRecyclerView() {
        final RecyclerView recyclerView;
        final MyDebtsAdapter adapter;

        List<MyDebt> myDebts = dataSource.getDebts();

        if (myDebts.size() == 0)
            addText("You don't have any debts", root.findViewById(R.id.my_debts_container));

        else {
            recyclerView = root.findViewById(R.id.my_debts);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutFrozen(true);

            adapter = new MyDebtsAdapter(this, myDebts);

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutFrozen(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private void addText(String text, ViewGroup container) {
        View layout = getLayoutInflater().inflate(R.layout.component_label, container);
        TextView noDebts = layout.findViewById(R.id.label_text);
        noDebts.setText(text);
    }


    @Override
    public void onArrange(MyDebt debt) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Transferring debt arrangement")
                    .setMessage(
                            "Are you sure want to transfer " + Formatter.formatMyDebtValue(debt) +
                                    " to " + debt.getTo().getUser().getName() + "?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) ->
                            arrangeMyDebt(debt))
                    .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onMarkAsArranged(MyDebt debt) {
        arrangeMyDebt(debt);
    }

    private void arrangeMyDebt(MyDebt debt) {
        final String memberId = debt.getFromMember();
        final double value =  debt.getValue();
        final String currency = debt.getCurrency();
        final String note = "Debt arrangement";
        final String roomKey = debt.getRoomKey();
        final ArrayList<String> included = new ArrayList<>();
        included.add(debt.getTo().getId());

        uploadDebtArrangement(roomKey, value, memberId, note, currency, included, debt);
    }

    private void uploadDebtArrangement(String roomKey, double value, String memberId, String note, String currency, ArrayList<String> included, MyDebt d) {
        LinearLayout progress = getActivity().findViewById(R.id.progressbar_home);
        progress.setVisibility(View.VISIBLE);

        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.addNewPayment(new APIRoutes.PaymentData(roomKey, value, memberId, note, currency, included)).enqueue(
                new DebterAPI.DebterCallback<>((call, response) -> {
                    getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE));
                    dataSource.getDebts().remove(d);
                    initializeMyDebtRecyclerView();
                },
                (__, ___) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE))));
    }
}
