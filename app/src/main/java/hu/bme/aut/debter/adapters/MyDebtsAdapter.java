package hu.bme.aut.debter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.MyDebt;

public class MyDebtsAdapter extends RecyclerView.Adapter {

    MyDebtOnClickListener listener;
    List<MyDebt> debts;

    public MyDebtsAdapter(MyDebtOnClickListener listener, List<MyDebt> myDebts) {
        this.listener = listener;
        this.debts = myDebts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View debtView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.component_my_debt, parent, false);
        return new MyDebtViewHolder(debtView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyDebt debt = debts.get(position);
        MyDebtViewHolder viewHolder = (MyDebtViewHolder)holder;
        viewHolder.debt = debt;

        viewHolder.to.setText(debt.getTo().getUser().getName());
        viewHolder.value.setText(Formatter.formatMyDebtValue(debt));
        viewHolder.room.setText(debt.getRoomName());
    }

    public void setNewDebts(List<MyDebt> debts) {
        this.debts.clear();
        this.debts.addAll(debts);
    }

    @Override
    public int getItemCount() {
        return debts.size();
    }

    public interface MyDebtOnClickListener{
        void onArrange(MyDebt debt);
        void onMarkAsArranged(MyDebt debt);
    }

    class MyDebtViewHolder extends RecyclerView.ViewHolder {
        TextView to;
        TextView value;
        TextView room;
        Button arrange;
        TextView markAsArranged;

        MyDebt debt;

        MyDebtViewHolder(@NonNull View roomView) {
            super(roomView);

            to = roomView.findViewById(R.id.my_debt_to);
            value = roomView.findViewById(R.id.my_debt_value);
            room = roomView.findViewById(R.id.debt_room);
            arrange = roomView.findViewById(R.id.my_debt_arrange);
            markAsArranged = roomView.findViewById(R.id.my_debt_mark_as_arranged);

            arrange.setOnClickListener(view -> listener.onArrange(debt));
            markAsArranged.setOnClickListener(view -> listener.onMarkAsArranged(debt));
        }
    }
}
