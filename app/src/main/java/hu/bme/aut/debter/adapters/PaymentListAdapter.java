package hu.bme.aut.debter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Payment;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentViewHolder> {

    private PaymentClickListener listener;
    private List<Payment> payments;

    public PaymentListAdapter (List<Payment> payments, PaymentClickListener listener) {
        this.listener = listener;
        this.payments = payments;
        this.payments.sort(this::sortDate);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View roomView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_payment, parent, false);
        return new PaymentListAdapter.PaymentViewHolder(roomView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);
        holder.payment = payment;

        holder.member.setText(payment.getMember().getUser().getName());
        holder.note.setText(payment.getNote());
        holder.value.setText(Formatter.formatPaymentValue(payment));
        holder.date.setText(Formatter.formatDate(payment.getDate()));
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void setPayments (List<Payment> payments) {
        this.payments = payments;
        this.payments.sort(this::sortDate);
    }

    public interface PaymentClickListener {
        void onPaymentClicked(Payment p);
    }

    private int sortDate(Payment p1, Payment p2) {
        return (int)((p2.getDate().getTime() - p1.getDate().getTime()) / 1000);
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {
        View listItem;
        TextView note;
        TextView member;
        TextView value;
        TextView date;
        Payment payment;

        PaymentViewHolder(@NonNull View paymentView) {
            super(paymentView);
            listItem = paymentView.findViewById(R.id.payment_list_item);
            note = paymentView.findViewById(R.id.payment_note);
            member = paymentView.findViewById(R.id.payment_member);
            value = paymentView.findViewById(R.id.payment_value);
            date = paymentView.findViewById(R.id.payment_date);
            this.listItem.setOnClickListener((view) -> listener.onPaymentClicked(payment));
        }
    }
}
