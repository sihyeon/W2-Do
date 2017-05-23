package com.team.codealmanac.w2do.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class SimpleInputDialog extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "SimpleInputDialog";
    private EditText act_simpleinput_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simpleinput);

        FontContract mFontContract = new FontContract(getApplication().getAssets());

        TextView act_simpleinput_header_text = (TextView) findViewById(R.id.act_simpleinput_header_text);
        act_simpleinput_edittext = (EditText) findViewById(R.id.act_simpleinput_edittext);
        Button act_simpleinput_cancel_btn = (Button) findViewById(R.id.act_simpleinput_cancel_btn);
        Button act_simpleinput_submit_btn = (Button) findViewById(R.id.act_simpleinput_submit_btn);

        act_simpleinput_edittext.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_header_text.setTypeface(mFontContract.FranklinGothic_MediumCond());
        act_simpleinput_cancel_btn.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_submit_btn.setTypeface(mFontContract.NahumSquareR_Regular());

        act_simpleinput_cancel_btn.setOnClickListener(this);
        act_simpleinput_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.act_simpleinput_submit_btn) {
            if (TextUtils.isEmpty(act_simpleinput_edittext.getText())) {
                act_simpleinput_edittext.setError("내용을 입력해주세요.");
                return;
            }

        } else {
            finish();
        }
    }
}
