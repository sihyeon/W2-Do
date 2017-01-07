package com.team.codealmanac.w2do;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.codealmanac.w2do.adapter.AddRemoveNumberedAdapter;


import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class APP2MAIN extends AppCompatActivity
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
        setContentView(R.layout.drawer_main);

        // toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // content_main의 recyclerview 설정
        recyclerView = (RecyclerView) findViewById(R.id.app2_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setBackgroundColor(Color.parseColor("#30000000"));
        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4));

        //content_main의 floating btn 설정
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //drawer_main -> drawer actionbartoggle 설정 부분
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view 리스너
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        //navigation header item 설정
        nav_user_image = (ImageView) findViewById(R.id.nav_user_image);
        nav_user_name = (TextView) findViewById(R.id.nav_user_name);
        nav_user_email = (TextView) findViewById(R.id.nav_user_email);

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
