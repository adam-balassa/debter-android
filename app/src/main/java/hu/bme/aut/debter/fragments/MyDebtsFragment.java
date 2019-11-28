package hu.bme.aut.debter.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.adapters.MyDebtsAdapter;
import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.data.UserDataSource;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.User;

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

        if (myDebts.size() == 0) {
            addText("You don't have any debts", root.findViewById(R.id.my_debts_container));
        }
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
        TextView noDebts = new TextView(getContext());
        noDebts.setText(text);
        noDebts.setGravity(Gravity.CENTER_HORIZONTAL);
        noDebts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        noDebts.setTextColor(Color.BLACK);

        container.addView(noDebts);
    }

    private List<Debt> getMyDebts(List<Member> members) {
        List<Debt> debts = new LinkedList<>();
        User me = UserDataSource.getInstance().getLoggedUser();
        for (Member member : members)
            if (member.getUser() == me)
                debts.addAll(member.getDebts());
        return debts;
    }


    @Override
    public void onArrange(MyDebt debt) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Transferring debt arrangement")
                    .setMessage(
                            "Are you sure want to transfer " + Formatter.formatMyDebtValue(debt) +
                                    " to " + debt.getTo().getUser().getName() + "?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) ->
                            Toast.makeText(getContext(), "Money transferred", Toast.LENGTH_SHORT).show())
                    .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onMarkAsArranged(MyDebt debt) {
        Toast.makeText(getContext(), "Debt marked as arranged ( "+  debt.getTo().getUser().getName() +" )", Toast.LENGTH_LONG).show();
    }
}
