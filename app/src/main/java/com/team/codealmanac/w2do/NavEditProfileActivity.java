package com.team.codealmanac.w2do;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.PreferencesManager;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by sihyeon on 2017-03-04.
 */

public class NavEditProfileActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener{
    private FirebaseUser mUser;

    private TextView nav_item_user_name;
    private TextView nav_item_user_email;
    private ImageView nav_item_user_image;
    private EditText nav_profile_edittext;

    private Button BackpressBtn;
    private Button nav_profile_edit_btn;
    private FontContract mFontContract;
    private DatabaseReference mNicknameReference;

    private PreferencesManager mPreferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navitem_editprofile);
        mPreferencesManager = new PreferencesManager(getApplicationContext());
        mFontContract = new FontContract(getApplication().getAssets());
        mNicknameReference = FirebaseDatabase.getInstance().getReference().child("nickname");

        nav_item_user_name = (TextView)findViewById(R.id.nav_item_user_name);
        nav_item_user_image = (ImageView)findViewById(R.id.nav_item_user_image);
        nav_item_user_email = (TextView)findViewById(R.id.nav_item_user_email);
        nav_profile_edittext = (EditText)findViewById(R.id.nav_profile_edittext);
        nav_profile_edit_btn = (Button)findViewById(R.id.nav_edit_profile_btn);
        BackpressBtn = (Button)findViewById(R.id.nav_profile_backpress_btn);

        mUser = getUserSession();
        CallProfileInfo(mUser);

        nav_item_user_name.setTypeface(mFontContract.NahumSquareB_Regular());
        nav_item_user_email.setTypeface(mFontContract.NahumSquareR_Regular());
        nav_profile_edittext.setTypeface(mFontContract.NahumSquareR_Regular());

        nav_profile_edittext.setOnEditorActionListener(this);
        nav_profile_edit_btn.setOnEditorActionListener(this);

        nav_profile_edit_btn.setOnClickListener(this);
        BackpressBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if(v.getId()==R.id.nav_profile_edittext && actionId==EditorInfo.IME_ACTION_DONE){
            String ChangedNickName = nav_profile_edittext.getText().toString();
            if (TextUtils.isEmpty(ChangedNickName)) {
                nav_profile_edittext.setError("Required");
            }
            mNicknameReference.child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue(ChangedNickName);
            mPreferencesManager.setNickname(ChangedNickName);
            Toast.makeText(this,"닉네임이 변경되었습니다.",Toast.LENGTH_SHORT).show();

        } /*else if(v.getId()==R.id.nav_edit_profile_btn && actionId==EditorInfo.IME_ACTION_DONE){
            String ChangedNickName = nav_profile_edittext.getText().toString();
            if (TextUtils.isEmpty(ChangedNickName)) {
                nav_profile_edittext.setError("Required");
            }
            mNicknameReference.child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue(ChangedNickName);
            PreferencesManager.setNickname(getApplicationContext(), ChangedNickName);
            Toast.makeText(this,"닉네임이 변경되었습니다.",Toast.LENGTH_SHORT).show();
        }*/
        return true;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.nav_profile_backpress_btn:
                onBackPressed();
                break;
            case R.id.nav_edit_profile_btn:
                nav_profile_edittext.setText(null);
                break;
        }
    }

    private void CallProfileInfo(FirebaseUser mUser){
        nav_profile_edittext.setText(mPreferencesManager.getNickname());
        nav_item_user_name.setText(mUser.getDisplayName());
        nav_item_user_email.setText(mUser.getEmail());

        if(mUser.getPhotoUrl() != null){
            Glide.with(getApplicationContext()).load(mUser.getPhotoUrl().toString())
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(nav_item_user_image);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.img_profile_none)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(nav_item_user_image);
        }
    }
}
