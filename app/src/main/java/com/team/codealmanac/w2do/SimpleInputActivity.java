package com.team.codealmanac.w2do;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class SimpleInputActivity extends Activity {

    private TextView headerText;
    private EditText todayedittext;
    private Button todayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simpleinput);

        headerText = (TextView)findViewById(R.id.today_header_text);
        todayedittext = (EditText) findViewById(R.id.today_simple_edit);
        todayButton = (Button)findViewById(R.id.today_add_btn);

    }

}
