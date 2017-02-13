package com.team.codealmanac.w2do;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.google.firebase.auth.FirebaseUser;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.fragment.CalendarFragment;
import com.team.codealmanac.w2do.fragment.TodoFolderListFragment;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.team.codealmanac.w2do.fragment.TodoSimpleListFragment;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DrawerLayout act_main_drawer_layout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private Menu menu;

    //navigation header items
    private ImageView act_main_nav_user_image;
    private TextView act_main_nav_user_name;
    private TextView act_main_nav_user_email;

    private Toolbar act_main_toolbar;
    private TextView act_main_greetingmsg;
    private TextView act_main_user_name;
    private FloatingActionsMenu act_main_appbar_floatingActionsMenu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_folder_floatingbtn;
    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_simpleInput_floatingbtn;
    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_detailInput_floatingbtn;
    boolean isFolderFragment = false;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase Realtime DB setting
        mUser = getUserSession();

        //프래그먼트 등록
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.act_main_todo_fragment_layout, TodoSimpleListFragment.newInstance());
        fragmentTransaction.commit();

        // act_main_toolbar 설정
        act_main_toolbar = (Toolbar) findViewById(R.id.act_main_toolbar);

        setSupportActionBar(act_main_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        //content_main의 floating btn 설정
        final FrameLayout act_main_appbar_floatingButtonLayout = (FrameLayout) findViewById(R.id.act_main_appbar_floatingButtonLayout);
        act_main_appbar_floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.act_main_appbar_floatingActionsMenu);
        act_main_appbar_floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                act_main_appbar_floatingButtonLayout.setVisibility(View.VISIBLE);
                act_main_appbar_floatingButtonLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        act_main_appbar_floatingActionsMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                act_main_appbar_floatingButtonLayout.setVisibility(View.GONE);
                act_main_appbar_floatingButtonLayout.setOnTouchListener(null);
            }
        });

        act_main_appbar_folder_floatingbtn = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.act_main_appbar_folder_floatingbtn);
        act_main_appbar_folder_floatingbtn.setStrokeVisible(false);
        act_main_appbar_folder_floatingbtn.setTitle("Add Folder");
        act_main_appbar_folder_floatingbtn.setVisibility(View.GONE);
        act_main_appbar_folder_floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act_main_appbar_floatingActionsMenu.collapse();
            }
        });

        act_main_appbar_simpleInput_floatingbtn = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.act_main_appbar_simpleInput_floatingbtn);
        act_main_appbar_simpleInput_floatingbtn.setStrokeVisible(false);
        act_main_appbar_simpleInput_floatingbtn.setTitle("Simple Text");
        act_main_appbar_simpleInput_floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SimpleInput = new Intent(MainActivity.this,SimpleInputActivity.class);
                startActivity(SimpleInput);
                act_main_appbar_floatingActionsMenu.collapse();
            }
        });

        act_main_appbar_detailInput_floatingbtn = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.act_main_appbar_detailInput_floatingbtn);
        act_main_appbar_detailInput_floatingbtn.setStrokeVisible(false);
        act_main_appbar_detailInput_floatingbtn.setTitle("Detail Text");
        act_main_appbar_detailInput_floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DetailInput = new Intent(MainActivity.this, DetailInputActivity.class);
                startActivity(DetailInput);
                act_main_appbar_floatingActionsMenu.collapse();
                overridePendingTransition(R.anim.push_out_left,R.anim.pull_in_right);
            }
        });

//        final ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
//        drawable.getPaint().setColor(getResources().getColor(R.color.white));

        //activity_main -> act_main_drawer_layout actionbartoggle 설정 부분
        act_main_drawer_layout = (DrawerLayout) findViewById(R.id.act_main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, act_main_drawer_layout, act_main_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /** Called when a act_main_drawer_layout has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a act_main_drawer_layout has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        act_main_drawer_layout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.btn_hambuger);
        mDrawerToggle.syncState();

        act_main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(act_main_drawer_layout.isDrawerOpen(Gravity.LEFT)){
                    act_main_drawer_layout.closeDrawer(Gravity.LEFT);
                } else {
                    act_main_drawer_layout.openDrawer(Gravity.LEFT);
                }
            }
        });

        //navigation view 리스너
        navigationView = (NavigationView) findViewById(R.id.act_main_nav_view);
        navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);

        //navigation header item 설정
        act_main_nav_user_image = (ImageView) v.findViewById(R.id.act_main_nav_user_image);
        act_main_nav_user_name = (TextView) v.findViewById(R.id.act_main_nav_user_name);
        act_main_nav_user_email = (TextView) v.findViewById(R.id.act_main_nav_user_email);

        // Fragment 상단 인사말 + 유저 이름 textview
        act_main_greetingmsg = (TextView) findViewById(R.id.act_main_greetingmsg);
        act_main_user_name = (TextView) findViewById(R.id.act_main_user_name);

        Typeface msgfont = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");
        act_main_greetingmsg.setTypeface(msgfont);
        act_main_user_name.setTypeface(msgfont);

    }

    @Override
    protected void onStart() {
        super.onStart();
        act_main_user_name.setText(PreferencesManager.getNickname(getApplicationContext()));

        act_main_nav_user_name.setText(mUser.getDisplayName());
        act_main_nav_user_email.setText(mUser.getEmail());

        if(mUser.getPhotoUrl().toString() != null){
            Glide.with(getApplicationContext()).load(mUser.getPhotoUrl().toString())
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(act_main_nav_user_image);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.btn_wtd)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(act_main_nav_user_image);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    // act_main_drawer_layout 상태 확인 후 act_main_drawer_layout oepn/close 함수
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
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        menu.findItem(R.id.menu_change_todo_frg).setChecked(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_todo_frg:
                act_main_appbar_folder_floatingbtn.setVisibility(View.VISIBLE);
                if(isFolderFragment){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out);
                    fragmentTransaction.replace(R.id.act_main_todo_fragment_layout, TodoSimpleListFragment.newInstance());
                    fragmentTransaction.commit();
                    isFolderFragment = false;
                    act_main_appbar_folder_floatingbtn.setVisibility(View.GONE);
                    item.setIcon(R.drawable.btn_gridview);
                }else{
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out);
                    fragmentTransaction.replace(R.id.act_main_todo_fragment_layout, TodoFolderListFragment.newInstance());
                    fragmentTransaction.commit();
                    isFolderFragment = true;
                    act_main_appbar_folder_floatingbtn.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.btn_listview);
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createNotification(){
        PendingIntent buttonIntent = PendingIntent.getActivity(this, 0, new Intent(this, SimpleInputActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icn_logo)
                .setContentTitle("test")
                .setContentText("tessssstt")
                .addAction(R.drawable.icn_memo, "간단입력", buttonIntent)
                .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    // nagivation 내부 item 선언
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            // 락스크린 액티비티 이동
            startActivity(new Intent(MainActivity.this, LockScreenActivity.class));
        } else if (id == R.id.nav_gallery) {
            createNotification();
        } else if (id == R.id.nav_slideshow) {
            //캘린더 item fragment
            act_main_appbar_floatingActionsMenu.setVisibility(View.GONE);
            act_main_user_name.setVisibility(View.GONE);
            act_main_greetingmsg.setVisibility(View.GONE);
            fragment = new CalendarFragment();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if(fragment != null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.act_main_todo_fragment_layout,fragment);
            fragmentTransaction.commit();
        }

        act_main_drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
