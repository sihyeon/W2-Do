package com.team.codealmanac.w2do;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import com.team.codealmanac.w2do.fragment.TodoSimpleListFragment;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private Menu menu;

    //navigation header items
    private ImageView nav_user_image;
    private TextView nav_user_name;
    private TextView nav_user_email;

    private Toolbar toolbar;
    private TextView fragment_greetingmsg;
    private TextView fragment_username;
    private FloatingActionsMenu floatingActionsMenu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionA;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionB;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton_actionC;
    boolean isFolderFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프래그먼트 등록
        Fragment defaultfragment = new TodoSimpleListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_todo_fragment_view, defaultfragment);
        fragmentTransaction.commit();

        // toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

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

        floatingActionButton_actionA = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        floatingActionButton_actionA.setStrokeVisible(true);
        floatingActionButton_actionA.setTitle("Add Folder");
        floatingActionButton_actionA.setVisibility(View.GONE);
        floatingActionButton_actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionsMenu.collapse();
            }
        });

        floatingActionButton_actionB = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
        floatingActionButton_actionB.setStrokeVisible(true);
        floatingActionButton_actionB.setTitle("Simple Text");
        floatingActionButton_actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SimpleInput = new Intent(MainActivity.this,SimpleInputActivity.class);
                startActivity(SimpleInput);
                floatingActionsMenu.collapse();
            }
        });

        floatingActionButton_actionC = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_c);
        floatingActionButton_actionC.setStrokeVisible(true);
        floatingActionButton_actionC.setTitle("Detail Text");
        floatingActionButton_actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DetailInput = new Intent(MainActivity.this, DetailInputActivity.class);
                startActivity(DetailInput);
                floatingActionsMenu.collapse();
                overridePendingTransition(R.anim.push_out_left,R.anim.pull_in_right);
            }
        });

        final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));


        //activity_main -> drawer actionbartoggle 설정 부분
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.btn_hambuger);
        mDrawerToggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

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
        fragment_greetingmsg = (TextView) findViewById(R.id.greetingmsg);
        fragment_username = (TextView) findViewById(R.id.user_name);

        FirebaseUser googleUserInfo = FirebaseAuth.getInstance().getCurrentUser();
        if (googleUserInfo != null) {
            nav_user_name.setText(googleUserInfo.getDisplayName());
            nav_user_email.setText(googleUserInfo.getEmail());
            if (googleUserInfo.getPhotoUrl() != null) {
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((TextView) findViewById(R.id.user_name)).setText(PreferencesManager.getNickname(getApplicationContext()));
    }

    // drawer 상태 확인 후 drawer oepn/close 함수
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }


     //menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_change_todo_frg).setChecked(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_todo_frg:
                floatingActionButton_actionA.setVisibility(View.VISIBLE);
                if(isFolderFragment){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out);
                    fragmentTransaction.replace(R.id.layout_todo_fragment_view, new TodoSimpleListFragment());
                    fragmentTransaction.commit();
                    isFolderFragment = false;
                    item.setIcon(R.drawable.btn_gridview);
                }else{
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out);
                    fragmentTransaction.replace(R.id.layout_todo_fragment_view, new TodoFolderListFragment());
                    fragmentTransaction.commit();
                    isFolderFragment = true;
                    item.setIcon(R.drawable.btn_listview);
                }
                break;

            default:
                break;
        }
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
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_todo_fragment_view,fragment);
            fragmentTransaction.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
