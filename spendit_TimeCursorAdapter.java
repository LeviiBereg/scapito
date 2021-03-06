package kvi.spendit;

/**
 * Created by Admin on 29.01.2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import kvi.spendit.data.TimeContract;

public class TimeCursorAdapter extends CursorAdapter {

    public TimeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_group, parent, false);
    }

    /**
     * This method binds the data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView groupTv = (TextView)view.findViewById(R.id.tGroupTv);
        TextView blankTv = (TextView)view.findViewById(R.id.tBlankTv);

        String groupStr = cursor.getString(cursor.getColumnIndex(TimeContract.DurationEntity.COLUMN_NAME_GROUP));
        String blankStr = cursor.getString(cursor.getColumnIndex(TimeContract.DurationEntity.COLUMN_NAME_TIME));

        groupTv.setText(groupStr);
        blankTv.setText(blankStr);
    }
}