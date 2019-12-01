package hu.bme.aut.debter.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.adapters.DebtsAdapter;
import hu.bme.aut.debter.adapters.MyDebtsAdapter;
import hu.bme.aut.debter.data.api.APIRoutes;
import hu.bme.aut.debter.data.api.DebterAPI;
import hu.bme.aut.debter.data.services.RoomDataSource;
import hu.bme.aut.debter.data.services.UserDataSource;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.DebterRoom;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.User;

public class DebtsFragment extends Fragment implements MyDebtsAdapter.MyDebtOnClickListener, DebtsAdapter.DebtOnClickListener {

    RoomDataSource dataSource;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_debts, container, false);
        this.dataSource = RoomDataSource.getInstance();

        initializeMyDebtRecyclerView();
        initializeDebtsRecyclerView();
        return root;
    }

    private void initializeMyDebtRecyclerView() {
        final RecyclerView recyclerView;
        final MyDebtsAdapter adapter;

        List<MyDebt> myDebts = getMyDebts(dataSource.getRoom().getValue().getMembers());

        if (myDebts.size() == 0) {
            addText("You don't have any debts", root.findViewById(R.id.my_debts_container));
        }
        else {
            recyclerView = root.findViewById(R.id.my_debts);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutFrozen(true);


            adapter = new MyDebtsAdapter(this, myDebts);
            RoomDataSource.getInstance().getRoom().observe(this, room -> adapter.setNewDebts(getMyDebts(room.getMembers())));

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutFrozen(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private void initializeDebtsRecyclerView() {
        final RecyclerView recyclerView;
        final DebtsAdapter adapter;

        List<Debt> myDebts = getDebts(dataSource.getRoom().getValue().getMembers());

        if (myDebts.size() == 0) {
            addText("All debts are arranged", root.findViewById(R.id.all_debts_container));
        }

        else {
            recyclerView = root.findViewById(R.id.all_debts);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutFrozen(true);

            adapter = new DebtsAdapter(this, myDebts);
            RoomDataSource.getInstance().getRoom().observe(this, room -> adapter.setNewDebts(getDebts(room.getMembers())));

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

    private List<MyDebt> getMyDebts(List<Member> members) {
        List<MyDebt> debts = new LinkedList<>();
        final DebterRoom room = dataSource.getRoom().getValue();
        final Member meMember = findMe(members);
        for (Debt d : meMember.getDebts())
            debts.add(new MyDebt(d.getTo(), d.getCurrency(), d.getValue(), room.getTitle(),
                    room.getRoomKey(), meMember.getId()));

        return debts;
    }

    private Member findMe (List<Member> members) {
        User me = UserDataSource.getInstance().getLoggedUser();
        for (Member member : members)
            if (member.getUser().getName().equals(me.getName()))
                return member;
        return null;
    }

    private List<Debt> getDebts(List<Member> members) {
        List<Debt> debts = new LinkedList<>();
        for (Member member : members)
            debts.addAll(member.getDebts());
        return debts;
    }

    public void onMarkAsArranged(Debt debt) {
        arrangeDebt(debt);
    }

    @Override
    public void onArrange(MyDebt debt) {

        new AlertDialog.Builder(getContext())
                .setTitle("Transferring debt arrangement")
                .setMessage(
                        "Are you sure want to transfer " + Formatter.formatMyDebtValue(debt) +
                                " to " + debt.getTo().getUser().getName() + "?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> arrangeMyDebt(debt))
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onMarkAsArranged(MyDebt debt) {
        arrangeMyDebt(debt);
    }

    private void arrangeDebt(Debt debt) {
        final DebterRoom room = dataSource.getRoom().getValue();

        final String memberId = debt.getFrom().getId();
        final double value =  debt.getValue();
        final String currency = debt.getCurrency();
        final String note = "Debt arrangement";
        final String roomKey = room.getRoomKey();
        final ArrayList<String> included = new ArrayList<>();
        included.add(debt.getTo().getId());

        uploadDebtArrangement(roomKey, value, memberId, note, currency, included);
    }

    private void arrangeMyDebt(MyDebt debt) {
        final DebterRoom room = dataSource.getRoom().getValue();

        final String memberId = room.findMemberByName(UserDataSource.getInstance().getLoggedUser().getName()).getId();
        final double value =  debt.getValue();
        final String currency = debt.getCurrency();
        final String note = "Debt arrangement";
        final String roomKey = room.getRoomKey();
        final ArrayList<String> included = new ArrayList<>();
        included.add(debt.getTo().getId());

        uploadDebtArrangement(roomKey, value, memberId, note, currency, included);
    }

    private void uploadDebtArrangement(String roomKey, double value, String memberId, String note, String currency, ArrayList<String> included) {
        LinearLayout progress = getActivity().findViewById(R.id.progressbar_view);
        progress.setVisibility(View.VISIBLE);

        APIRoutes api = DebterAPI.getInstance().getDebter();
        api.addNewPayment(new APIRoutes.PaymentData(roomKey, value, memberId, note, currency, included)).enqueue(
            new DebterAPI.DebterCallback<>((call, response) -> {
                RoomDataSource.getInstance().loadRoomDetails(roomKey,
                        new DebterAPI.DebterCallback<>((call1, response1) -> {
                            getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE));
                            Navigation.findNavController(root).navigateUp();
                        }));
            },
            (__, ___) -> getActivity().runOnUiThread(() -> progress.setVisibility(View.GONE))));
    }
}
