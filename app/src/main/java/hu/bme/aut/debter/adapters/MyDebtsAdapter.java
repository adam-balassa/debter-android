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

public class MyDebtsAdapter extends RecyclerView.Adapter {

    MyDebtOnClickListener listener;
    List<Debt> debts;

    public MyDebtsAdapter(MyDebtOnClickListener listener, List<Debt> myDebts) {
        this.listener = listener;
        this.debts = myDebts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View debtView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.my_debt, parent, false);
        return new MyDebtViewHolder(debtView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Debt debt = debts.get(position);
        MyDebtViewHolder viewHolder = (MyDebtViewHolder)holder;
        viewHolder.debt = debt;

        viewHolder.from.setText(debt.getFrom().getUser().getName());
        viewHolder.to.setText(debt.getTo().getUser().getName());
        viewHolder.value.setText(Formatter.formatDebtValue(debt));
    }

    public void setNewDebts(List<Debt> debts) {
        this.debts.clear();
        this.debts.addAll(debts);
    }

    @Override
    public int getItemCount() {
        return debts.size();
    }

    public interface MyDebtOnClickListener{
        void onArrange(Debt debt);
        void onMarkAsArranged(Debt debt);
    }

    class MyDebtViewHolder extends RecyclerView.ViewHolder {
        TextView from;
        TextView to;
        TextView value;
        Button arrange;
        TextView markAsArranged;

        Debt debt;

        MyDebtViewHolder(@NonNull View roomView) {
            super(roomView);

            from = roomView.findViewById(R.id.my_debt_from);
            to = roomView.findViewById(R.id.my_debt_to);
            value = roomView.findViewById(R.id.my_debt_value);
            arrange = roomView.findViewById(R.id.my_debt_arrange);
            markAsArranged = roomView.findViewById(R.id.my_debt_mark_as_arranged);

            arrange.setOnClickListener(view -> listener.onArrange(debt));
            markAsArranged.setOnClickListener(view -> listener.onMarkAsArranged(debt));
        }
    }
}
