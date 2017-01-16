package com.team.codealmanac.w2do;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class DetailInputActivity extends AppCompatActivity{

    private TextView act_detailInout_folder_select;
    private Spinner act_detailInput_folder_spinner;
    private Toolbar act_detailInput_toolbar;
    private TextView act_detailInput_date_select;
    private EditText act_detailInput_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailinput);

        // toolbar 설정
        act_detailInput_toolbar = (Toolbar) findViewById(R.id.act_detailInput_toolbar);
        setSupportActionBar(act_detailInput_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String[] Folder_Spinner_item = getResources().getStringArray(R.array.folder);
        ArrayAdapter<String> Folder_Spinner_Adapter = new ArrayAdapter<String>(
                this, R.layout.activity_detailinput_folder_spinner_textview, Folder_Spinner_item);

        act_detailInout_folder_select = (TextView) findViewById(R.id.act_detailInput_folder_select);
        act_detailInput_folder_spinner = (Spinner) findViewById(R.id.act_detailInput_folder_select_spinner);
        act_detailInput_date_select = (TextView) findViewById(R.id.act_detailInput_date_select);
        act_detailInput_editText = (EditText) findViewById(R.id.act_detailInput_editText);

        Folder_Spinner_Adapter.setDropDownViewResource(R.layout.activity_detailinput_folder_spinner_textview);
        act_detailInput_folder_spinner.setAdapter(Folder_Spinner_Adapter);

    }

}
