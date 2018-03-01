package kvi.spendit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Admin on 29.01.2018.
 */

public class TimeProvider extends ContentProvider {

    public static final String LOG_TAG = TimeProvider.class.getSimpleName();
    private DurationDbHelper dh;
    private final static int DURATION = 100;
    private final static int DURATION_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(TimeContract.CONTENT_AUTHORITY, TimeContract.PATH_DURATION, DURATION);
        sUriMatcher.addURI(TimeContract.CONTENT_AUTHORITY, TimeContract.PATH_DURATION + "/#", DURATION_ID);
    }

    @Override
    public boolean onCreate() {
        dh = new DurationDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dh.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch(match) {
            case DURATION:
                cursor = database.query(TimeContract.DurationEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DURATION_ID:
                selection = TimeContract.DurationEntity._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(TimeContract.DurationEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DURATION:
                return TimeContract.DurationEntity.CONTENT_LIST_TYPE;
            case DURATION_ID:
                return TimeContract.DurationEntity.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = dh.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri uriRes = null;

        switch(match) {
            case DURATION:
                long id = database.insert(TimeContract.DurationEntity.TABLE_NAME, null, contentValues);
                if(id != -1)
                    uriRes = ContentUris.withAppendedId(uri, id);
                else
                    Log.e(LOG_TAG, "Row insertion error for URI: " + uri);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert on URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriRes;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dh.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numOfDeletedRows = 0;

        switch (match) {
            case DURATION:
                numOfDeletedRows = database.delete(TimeContract.DurationEntity.TABLE_NAME, selection, selectionArgs);
                break;
            case DURATION_ID:
                selection = TimeContract.DurationEntity._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numOfDeletedRows = database.delete(TimeContract.DurationEntity.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if(numOfDeletedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return numOfDeletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(TimeContract.DurationEntity.COLUMN_NAME_GROUP)) {
            String name = contentValues.getAsString(TimeContract.DurationEntity.COLUMN_NAME_GROUP);
            if (name == null) {
                throw new IllegalArgumentException("Empty group name");
            }
        }
        if (contentValues.size() == 0) {
            return 0;
        }
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dh.getWritableDatabase();
        int numOfUpdatedRows = 0;
        switch (match) {
            case DURATION:
                numOfUpdatedRows = database.update(TimeContract.DurationEntity.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case DURATION_ID:
                selection = TimeContract.DurationEntity._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                numOfUpdatedRows = database.update(TimeContract.DurationEntity.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (numOfUpdatedRows != 0)
             getContext().getContentResolver().notifyChange(uri, null);
        return numOfUpdatedRows;
    }
}
