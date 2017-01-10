package com.team.codealmanac.w2do;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.fragment.CalendarFragment;
import com.team.codealmanac.w2do.fragment.TodoFolderListFragment;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

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

    private Toolbar toolbar;
    private TextView fragment_greetingmsg;
    private TextView fragment_username;
    private FloatingActionsMenu floatingActionsMenu;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionA;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionB;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionC;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //content_main의 floating btn 설정
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(160);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        floatingActionsMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        floatingActionButton_actionA = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.action_a);
        floatingActionButton_actionA.setStrokeVisible(true);
        floatingActionButton_actionA.setTitle("Add Folder");
        floatingActionButton_actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                floatingActionButton_actionA.setTitle("Simple Text");
            }
        });

        floatingActionButton_actionB = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.action_b);
        floatingActionButton_actionB.setStrokeVisible(true);
        floatingActionButton_actionB.setTitle("Simple Text");
        floatingActionButton_actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                floatingActionButton_actionB.setTitle("Detail Text");
            }
        });

        floatingActionButton_actionC = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.action_c);
        floatingActionButton_actionC.setStrokeVisible(true);
        floatingActionButton_actionC.setTitle("Detail Text");
        floatingActionButton_actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                floatingActionButton_actionB.setTitle("Detail Text");
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));


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

//        nav_user_name.setText("Test");
        // Fragment 상단 인사말 + 유저 이름 textview
        fragment_greetingmsg = (TextView)findViewById(R.id.greetingmsg);
        fragment_username = (TextView)findViewById(R.id.user_name) ;

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

    private void setFloatingActionButton(){

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
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            //캘린더 item fragment
            floatingActionsMenu.setVisibility(View.GONE);
            fragment_username.setVisibility(View.GONE);
            fragment_greetingmsg.setVisibility(View.GONE);
            fragment = new CalendarFragment();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if(fragment != null){
            FragmentTransaction fragmentitems = getSupportFragmentManager().beginTransaction();
            fragmentitems.replace(R.id.layout_todo_fragments,fragment);
            fragmentitems.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
