package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.data.services.RoomDataSource;
import hu.bme.aut.debter.data.services.UserDataSource;
import hu.bme.aut.debter.helper.Formatter;
import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.DebterRoom;
import hu.bme.aut.debter.model.Member;
import hu.bme.aut.debter.model.Payment;
import hu.bme.aut.debter.model.User;

public class HomeFragment extends Fragment {

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        configureNewPaymentButton();

        createAndShowStatistics();

        return root;
    }

    private void createAndShowStatistics() {
        DebterRoom debterRoom = RoomDataSource.getInstance().getRoom().getValue();
        User me = UserDataSource.getInstance().getLoggedUser();
        Member meMember = null;

        for (Member member: debterRoom.getMembers())
            if (member.getUser().getName().equals(me.getName())) {
                meMember = member;
                break;
            }

        int includedInPayments = 0;
        double total = 0.0;
        for (Payment payment : debterRoom.getPayments()) {
            if (payment.getMember() == meMember)
                total += payment.getRealValue();
            if (payment.getIncluded().contains(meMember))
                includedInPayments++;
        }

        double debt = 0.0;
        for (Debt d : meMember.getDebts())
            debt += d.getValue();

        String mainCurrency = debterRoom.getCurrency();
        double mainRounding = debterRoom.getRounding();


        TextView includedIn = root.findViewById(R.id.included_in);
        TextView payedTotal = root.findViewById(R.id.total_payed);
        TextView totalDebt = root.findViewById(R.id.total_debts);
        TextView rounding = root.findViewById(R.id.main_rounding);
        TextView currency = root.findViewById(R.id.main_currency);
        TextView title = root.findViewById(R.id.main_title);
        TextView roomKey = root.findViewById(R.id.main_room_key);


        includedIn.setText(includedInPayments + "");
        payedTotal.setText(Formatter.formatValue(total, mainCurrency));
        totalDebt.setText(Formatter.formatValue(debt, mainCurrency));
        rounding.setText(mainRounding + "");
        currency.setText(mainCurrency);
        title.setText(debterRoom.getTitle());
        roomKey.setText(debterRoom.getRoomKey());

    }

    private void configureNewPaymentButton() {
        FloatingActionButton fab = root.findViewById(R.id.fab);
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(android.R.anim.fade_in)
                .setExitAnim(android.R.anim.fade_out)
                .setPopExitAnim(android.R.anim.fade_out)
                .build();

        fab.setOnClickListener(view -> Navigation.findNavController(root).navigate(R.id.nav_new_payment, new Bundle(), navOptions));
    }

}