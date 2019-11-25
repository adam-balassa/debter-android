package hu.bme.aut.debter.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.List;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.Payment;
import hu.bme.aut.debter.fragments.history.HistoryViewModel;

public class PaymentFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        initData(root);
        return root;
    }

    private void initData(View root) {
        Payment payment = HistoryViewModel.getInstance().getActivePayment();
        TextView note = root.findViewById(R.id.payment_details_note);
        TextView member = root.findViewById(R.id.payment_details_member);
        TextView date = root.findViewById(R.id.payment_details_date);
        TextView value = root.findViewById(R.id.payment_details_value);

        note.setText(payment.getNote());
        member.setText(payment.getMember().getUser().getName());
        date.setText(Formatter.formatFullDate(payment.getDate()));
        value.setText(Formatter.formatPaymentValue(payment));

        initIncluded(root);
    }

    private void initIncluded(View root) {
        FlexboxLayout paymentIncluded = root.findViewById(R.id.payment_included);
        List<Member> members = RoomDataSource.getInstance().getRoom().getValue().getMembers();
        Payment payment = HistoryViewModel.getInstance().getActivePayment();
        for (Member member : members)
            addChip(paymentIncluded, member.getUser().getName(), payment.getIncluded().contains(member));
    }

    private void addChip (FlexboxLayout root, String name, boolean included) {
        View includedChip = getLayoutInflater().inflate(R.layout.component_chip, root, false);
        Chip chip = includedChip.findViewById(R.id.included_chip);
        if (included)
            markIncluded(chip);
        chip.setText(name);
        root.addView(includedChip);
    }

    private void markIncluded(Chip chip) {
        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentLight)));
        chip.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        chip.setChipStrokeWidth(2);
    }
}
