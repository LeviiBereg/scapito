package kvi.spendit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import kvi.spendit.data.TimeContract.DurationEntity;

/**
 * Created by Admin on 29.01.2018.
 */

public class DurationDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "spendit.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DurationEntity.TABLE_NAME + " (" +
            DurationEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DurationEntity.COLUMN_NAME_GROUP + " Text, " +
            DurationEntity.COLUMN_NAME_TIME + " Text, " +
            DurationEntity.COLUMN_NAME_DATE + " Text, " +
            DurationEntity.COLUMN_NAME_NOTE + " Text" + ");";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DurationEntity.TABLE_NAME;

    public DurationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
