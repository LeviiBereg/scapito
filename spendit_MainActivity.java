package kvi.spendit;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.time.Duration;
import java.util.List;

import kvi.spendit.data.TimeContract;
import kvi.spendit.data.TimeContract.DurationEntity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Date start;
    private Date pause;
    private long pauseDuration;
    private long duration;

    private ToggleButton stBtn;
    private ToggleButton pBtn;
    private Button saBtn;
    private ListView grLst;

    private static final int TIME_LOADER = 0;
    TimeCursorAdapter tca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pauseDuration = duration = 0;

        saBtn = (Button)findViewById(R.id.saveBtn);

        stBtn = (ToggleButton) findViewById(R.id.startStopTglBtn);
        stBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    stBtn.setText("Stop");
                    start = Calendar.getInstance().getTime();
                    duration = pauseDuration = 0;
                }
                else {
                    stBtn.setText("Start");
                    if(pBtn.isChecked())
                        pBtn.setChecked(false);
                    duration = Calendar.getInstance().getTime().getTime() - start.getTime() - pauseDuration;
                }

                pBtn.setEnabled(isChecked);
                saBtn.setEnabled(!isChecked);
            }
        });

        pBtn = (ToggleButton) findViewById(R.id.pauseTglBtn);
        pBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    pause = Calendar.getInstance().getTime();
                }
                else {
                    pauseDuration += Calendar.getInstance().getTime().getTime() - pause.getTime();
                }
            }
        });

        String[] projection = { "DISTINCT " + DurationEntity._ID, DurationEntity.COLUMN_NAME_GROUP, DurationEntity.COLUMN_NAME_TIME};

        //TODO: Add thread for list filling from database
        grLst = (ListView)findViewById(R.id.tGroupsLst);
        tca = new TimeCursorAdapter(this, null);
        grLst.setAdapter(tca);
        grLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditText groupTxt = (EditText)findViewById(R.id.tGroupTxt);
                //groupTxt.setText(adapterView.getSelectedItem());
            }
        });
        getLoaderManager().initLoader(TIME_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO: Delete naive saving visualisation
    public void saveResult(View view){
        ContentValues values = new ContentValues();
        values.put(DurationEntity.COLUMN_NAME_GROUP, ((EditText)findViewById(R.id.tGroupTxt)).getText().toString());
        values.put(DurationEntity.COLUMN_NAME_TIME, formatLongDurationToString(duration));
        values.put(DurationEntity.COLUMN_NAME_DATE, start.toString());
        values.put(DurationEntity.COLUMN_NAME_NOTE, ((EditText)findViewById(R.id.noteTxt)).getText().toString());

        Uri newUri = getContentResolver().insert(DurationEntity.CONTENT_URI, values);
        if(newUri == null)
            Toast.makeText(this, "Insertion failure!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    //TODO: Rework to StringBuilder logic
    public String formatLongDurationToString(long lmills){
        final int MILLS_IN_H = 3600000;
        final int MILLS_IN_M = 60000;
        final int MILLS_IN_S = 1000;
        String hrs = "" + (lmills / MILLS_IN_H);
        if(hrs.length() == 1)
            hrs = 0 + hrs;
        lmills = lmills % MILLS_IN_H;
        String mins = "" + (lmills / MILLS_IN_M);
        if(mins.length() == 1)
            mins = 0 + mins;
        lmills = lmills % MILLS_IN_M;
        String secs = "" + (lmills / MILLS_IN_S);
        if(secs.length() == 1)
            secs = 0 + secs;
        lmills = lmills % MILLS_IN_S;
        String mills = "" + lmills;
        for(int i = mills.length(); i <= 3; i++)
            mills = 0 + mills;
        return hrs + ":" + mins + ":" + secs + "." + mills;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                DurationEntity._ID,
                DurationEntity.COLUMN_NAME_GROUP,
                DurationEntity.COLUMN_NAME_TIME };
        return new CursorLoader(
                this,
                DurationEntity.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        tca.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tca.swapCursor(null);
    }
}
