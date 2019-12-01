package hu.bme.aut.debter.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import hu.bme.aut.debter.data.services.UsersDataSource;
import hu.bme.aut.debter.model.User;


public class AddUserAdapter extends ArrayAdapter<String> {

    List<User> suggestions;
    UsersDataSource dataSource;

    public AddUserAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        dataSource = UsersDataSource.getInstance();
        suggestions = new LinkedList<>();
    }

    public void refreshSuggestions(String charSequence) {
        this.suggestions = dataSource.getSuggestedUsers(charSequence);
        for (User u : suggestions)
            Log.d("develop", u.getName());
        Log.d("develop", " ");
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        User u = suggestions.get(position);
        return u.getName() + " - " + u.getEmail();
    }
}
