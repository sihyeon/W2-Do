package com.team.codealmanac.w2do;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team.codealmanac.w2do.adapter.AddRemoveNumberedAdapter;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.fragment.TodoFolderListFragment;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    //navigation header items
    private ImageView nav_user_image;
    private TextView nav_user_name;
    private TextView nav_user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프래그먼트 등록
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.layout_todo_fragments, new TodoFolderListFragment());
        fragmentTransaction.commit();

        // toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //content_main의 floating btn 설정
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //activity_main -> drawer actionbartoggle 설정 부분
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view 리스너
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        //navigation header item 설정
        nav_user_image = (ImageView) v.findViewById(R.id.nav_user_image);
        nav_user_name = (TextView) v.findViewById(R.id.nav_user_name);
        nav_user_email = (TextView) v.findViewById(R.id.nav_user_email);

        nav_user_name.setText("Test");

        FirebaseUser googleUserInfo = FirebaseAuth.getInstance().getCurrentUser();
        if(googleUserInfo != null){
            nav_user_name.setText(googleUserInfo.getDisplayName());
            nav_user_email.setText(googleUserInfo.getEmail());
            if(googleUserInfo.getPhotoUrl() != null ) {
                String personPhotoUrl = googleUserInfo.getPhotoUrl().toString();
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(nav_user_image);
            } else {
                Glide.with(getApplicationContext()).load(R.drawable.btn_wtd)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(nav_user_image);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((TextView)findViewById(R.id.user_name)).setText(PreferencesManager.getNickname(getApplicationContext()));
    }

    // drawer 상태 확인 후 drawer oepn/close 함수
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    // nagivation 내부 item 선언
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
