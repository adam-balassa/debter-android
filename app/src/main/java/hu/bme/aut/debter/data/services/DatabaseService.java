package hu.bme.aut.debter.data.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.debter.data.sqlite.DatabaseHelper;
import hu.bme.aut.debter.data.sqlite.LoggedUserTable;
import hu.bme.aut.debter.model.User;

public class DatabaseService {
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseService(Context context) {
        helper = new DatabaseHelper(context);
    }

    public User getLoggedUser() {
        final List<User> users = new ArrayList<>();
        final Cursor cursor = database.query(LoggedUserTable.TABLE_NAME, LoggedUserTable.getColumns(), null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            users.add(userFromCursor(cursor));
        if (users.size() == 0) return null;
        return users.get(0);
    }

    public void saveLoggedUser(User user) {
        final ContentValues values = new ContentValues();
        values.put(LoggedUserTable.Columns.name.name(), user.getName());
        values.put(LoggedUserTable.Columns.email.name(), user.getEmail());
        database.insert(LoggedUserTable.TABLE_NAME, null, values);
    }

    public void logout() {
        database.delete(LoggedUserTable.TABLE_NAME, null, null);
    }

    private User userFromCursor(Cursor cursor) {
        String name = cursor.getString(LoggedUserTable.Columns.name.ordinal());
        String email = cursor.getString(LoggedUserTable.Columns.email.ordinal());
        return new User(name, email);
    }

    public void open() throws SQLiteException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }
}
