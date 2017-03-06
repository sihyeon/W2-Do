package com.team.codealmanac.w2do;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.team.codealmanac.w2do.database.PreferencesManager;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by sihyeon on 2017-03-04.
 */

public class NavEditProfileActivity extends BaseActivity {
    private FirebaseUser mUser;
    private TextView nav_item_user_nickname;
    private TextView nav_item_user_name;
    private TextView nav_item_user_email;
    private ImageView nav_item_user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navitem_editprofile);

        nav_item_user_nickname = (TextView)findViewById(R.id.nav_item_user_nickname);
        nav_item_user_name = (TextView)findViewById(R.id.nav_item_user_name);
        nav_item_user_image = (ImageView)findViewById(R.id.nav_item_user_image);
        nav_item_user_email = (TextView)findViewById(R.id.nav_item_user_email);

        mUser = getUserSession();
        CallProfileInfo(mUser);

    }

    private void CallProfileInfo(FirebaseUser mUser){
        nav_item_user_nickname.setText(PreferencesManager.getNickname(getApplicationContext()));
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
