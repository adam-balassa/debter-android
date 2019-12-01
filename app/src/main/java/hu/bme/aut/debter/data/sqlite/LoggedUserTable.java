package hu.bme.aut.debter.data.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LoggedUserTable {
    public static final String TABLE_NAME = "LoggedUser";

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "("
            + Columns._id.name() + " integer primary key autoincrement, "
            + Columns.name.name() + "  text null, "
            + Columns.email.name() + " text not null);";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        Log.w(LoggedUserTable.class.getName(), "Upgrading from version " + oldVersion + " to " + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static String[] getColumns() {
        String[] columns = {
                Columns._id.name(),
                Columns.name.name(),
                Columns.email.name()
        };
        return columns;
    }

    public enum Columns {
        _id,
        name,
        email
    }
}
