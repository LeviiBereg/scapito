package kvi.spendit.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 29.01.2018.
 */

public final class TimeContract {

    public static final String CONTENT_AUTHORITY = "kvi.spendit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DURATION = "duration";

    private TimeContract(){}

    public static class DurationEntity implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DURATION);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of durations.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DURATION;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single duration.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DURATION;

        public static final String TABLE_NAME = "duration";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_GROUP = "tgroup";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
    }
}
