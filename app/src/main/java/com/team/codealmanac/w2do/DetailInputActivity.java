package com.team.codealmanac.w2do;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class DetailInputActivity extends AppCompatActivity {
    private Spinner act_detailInput_folder_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        String[] Folder_Spinner_item = getResources().getStringArray(R.array.folder);
        ArrayAdapter<String> Folder_Spinner_Adapter = new ArrayAdapter<String>(
                this, R.layout.activity_detailinput_folder_spinner_textview, Folder_Spinner_item);
        Folder_Spinner_Adapter.setDropDownViewResource(R.layout.activity_detailinput_folder_spinner_textview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit_btn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

