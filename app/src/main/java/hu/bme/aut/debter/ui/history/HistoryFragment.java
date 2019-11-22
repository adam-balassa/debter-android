package hu.bme.aut.debter.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.RoomActivity;
import hu.bme.aut.debter.adapters.PaymentListAdapter;
import hu.bme.aut.debter.data.RoomDataSource;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.history_fragment, container, false);

        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View root) {
        final RecyclerView recyclerView;
        final PaymentListAdapter adapter;

        recyclerView = root.findViewById(R.id.history_payments);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutFrozen(true);

        adapter = new PaymentListAdapter(RoomDataSource.getInstance().getRoom().getValue().getPayments(),
                payment -> ((RoomActivity)getActivity()).navigateToPayment());

        RoomDataSource.getInstance().getRoom().observe(this, room -> adapter.setPayments(room.getPayments()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
