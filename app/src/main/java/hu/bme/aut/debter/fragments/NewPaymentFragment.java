package hu.bme.aut.debter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hu.bme.aut.debter.R;
import hu.bme.aut.debter.data.APIRoutes;
import hu.bme.aut.debter.data.DebterAPI;
import hu.bme.aut.debter.data.RoomDataSource;
import hu.bme.aut.debter.model.DebterRoom;
import hu.bme.aut.debter.model.Member;

public class NewPaymentFragment extends Fragment {

    Set<Member> selectedMembers;
    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedMembers = new HashSet<>();

        root = inflater.inflate(R.layout.fragment_new_payment, container, false);
        RoomDataSource roomDataSource = RoomDataSource.getInstance();
        List<Member> members = roomDataSource.getRoom().getValue().getMembers();

        Spinner whoPayed = root.findViewById(R.id.new_payment_who_payed);
        List<String> memberNames = new LinkedList<>();
        for (Member m : members)
            memberNames.add(m.getUser().getName());
        whoPayed.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, memberNames.toArray()));

        Spinner currencies = root.findViewById(R.id.new_payment_currencies);
        String[] currencyValues = { "HUF", "EUR", "USD" };
        currencies.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, currencyValues));

        Button button = root.findViewById(R.id.upload_new_payment);

        button.setOnClickListener(view -> uploadNewPayment());

        showMemberChips(root, members);

        selectedMembers.addAll(members);

        return root;
    }

    private void uploadNewPayment() {
        LinearLayout progress = getActivity().findViewById(R.id.progressbar_view);
        progress.setVisibility(View.VISIBLE);
        final DebterRoom room = RoomDataSource.getInstance().getRoom().getValue();

        final String memberName = (String) ((Spinner) root.findViewById(R.id.new_payment_who_payed)).getSelectedItem();
        final String memberId = room.findMemberByName(memberName).getId();
        final double value =  Double.parseDouble(((EditText) root.findViewById(R.id.new_payment_value)).getText().toString());
        final String currency = (String) ((Spinner) root.findViewById(R.id.new_payment_currencies)).getSelectedItem();
        final String note = ((EditText) root.findViewById(R.id.new_payment_note)).getText().toString();
        final String roomKey = room.getRoomKey();
        final ArrayList<String> included = new ArrayList<>();
        for (Member member : selectedMembers)
            included.add(member.getId());

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

    private void showMemberChips(View root, List<Member> members) {
        FlexboxLayout layout = root.findViewById(R.id.payment_included);
        for (Member member : members)
            addChip(layout, member);
    }

    private void addChip (FlexboxLayout root, Member member) {
        View includedChip = getLayoutInflater().inflate(R.layout.compnent_selectable_member_chip, root, false);
        Chip chip = includedChip.findViewById(R.id.selectable_member_chip);
        chip.setText(member.getUser().getName());

        chip.setChecked(true);

        chip.setOnCheckedChangeListener((view, checked) -> {
            if (!checked)
                selectedMembers.remove(member);
            else
                selectedMembers.add(member);
        });


        root.addView(includedChip);
    }
}
