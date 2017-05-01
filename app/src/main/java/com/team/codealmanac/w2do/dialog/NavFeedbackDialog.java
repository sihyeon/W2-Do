package com.team.codealmanac.w2do.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseError;
import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-05-02.
 */

public class NavFeedbackDialog extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.navitem_feedbackcenter);
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.act_nav_feedback_sendbtn){
            Intent mail = new Intent(Intent.ACTION_SEND);
            String[] mailaddr = {"sihyun2139@gmail.com"};

            mail.setType("Plain/text");
            mail.putExtra(Intent.EXTRA_EMAIL,mailaddr);
            mail.putExtra(Intent.EXTRA_TEXT,"The email body text");
            startActivity(Intent.createChooser(mail,"Choose email client"));
        }
    }

}
