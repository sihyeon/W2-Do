package com.team.codealmanac.w2do.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.Pref;

    /* 구글 로그인 및 firebase 인증 순서
        (onCreate)GoogleSignInOptions 객체 생성 -> (onCreate)mGoogleApiClient 객체 생성 -> (onStart)AuthStateListener 등록 ->
        (f)signIn() -> (f)onActivityResult() -> (firebaseAuthWithGoogle)AuthCredential 호출 -> 호출 성공 시 AuthStateListener의 onAuthStateChanged 콜백
     */

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "WelcomeActivity";

    private EditText act_login_nickname_edit;
    private Button act_login_nick_input_btn;

    private Pref mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mPref = new Pref(getApplicationContext());

        Typeface font = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");

        TextView act_login_nickname_msg = (TextView)findViewById(R.id.act_login_nickname_msg);
        act_login_nickname_edit = (EditText)findViewById(R.id.act_login_nickname_edit);
        act_login_nick_input_btn = (Button)findViewById(R.id.act_login_nick_input_btn);
        act_login_nick_input_btn.setOnClickListener(this);
        act_login_nickname_msg.setTypeface(font);
        act_login_nickname_edit.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_login_nick_input_btn:           //닉네임 입력 버튼 클릭
                String nickname = act_login_nickname_edit.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                    act_login_nickname_edit.setError("Required");
                    return;
                }
                mPref.setNickname(nickname);

                Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainIntent);
                finish();
                break;
        }
    }
}
