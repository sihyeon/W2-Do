package com.team.codealmanac.w2do;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.PreferencesManager;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.team.codealmanac.w2do.dialog.FolderInputDialogFragment;
import com.team.codealmanac.w2do.dialog.SimpleInputDialog;
import com.team.codealmanac.w2do.fragment.TodoFolderListFragment;
import com.team.codealmanac.w2do.fragment.TodoSimpleListFragment;

import java.util.Calendar;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout act_main_drawer_layout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    //navigation header items
    private ImageView act_main_nav_user_image;
    private TextView act_main_nav_user_name;
    private TextView act_main_nav_user_email;

    private Toolbar act_main_toolbar;
    private TextView act_main_greetingmsg;
    private TextView act_main_user_name;
    private FloatingActionsMenu act_main_appbar_floatingActionsMenu;

    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_folder_floatingbtn;
    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_simpleInput_floatingbtn;
    private com.getbase.floatingactionbutton.FloatingActionButton act_main_appbar_detailInput_floatingbtn;
    boolean isFolderFragment = false;

    private FirebaseUser mUser;

    private Fragment mTodoSimpleListFragment;
    private Fragment mTodoFolderListFragment;

    private FontContract mFontContract;

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

        mFontContract = new FontContract(getApplication().getAssets());

        //투두 심플, 폴더리스트 프래그먼트 설정
        mTodoSimpleListFragment = TodoSimpleListFragment.newInstance();
        mTodoFolderListFragment = TodoFolderListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.act_main_todo_fragment_layout, mTodoSimpleListFragment)
                .add(R.id.act_main_todo_fragment_layout, mTodoFolderListFragment)
                .hide(mTodoFolderListFragment)
                .commit();

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
                FolderInputDialogFragment.newInstance().show(getFragmentManager(), "test");
                act_main_appbar_floatingActionsMenu.collapse();
            }
        });

        act_main_appbar_simpleInput_floatingbtn = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.act_main_appbar_simpleInput_floatingbtn);
        act_main_appbar_simpleInput_floatingbtn.setStrokeVisible(false);
        act_main_appbar_simpleInput_floatingbtn.setTitle("Simple Text");
        act_main_appbar_simpleInput_floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SimpleInput = new Intent(MainActivity.this,SimpleInputDialog.class);
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
                if(act_main_drawer_layout.isDrawerOpen(GravityCompat.START)){
                    act_main_drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    act_main_drawer_layout.openDrawer(GravityCompat.START);
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

        // navigaion header font 설정
        act_main_nav_user_name.setTypeface(mFontContract.NahumSquareB_Regular());
        act_main_nav_user_email.setTypeface(mFontContract.NahumSquareR_Regular());

        // Fragment 상단 인사말 + 유저 이름 textview
        act_main_greetingmsg = (TextView) findViewById(R.id.act_main_greetingmsg);
        act_main_user_name = (TextView) findViewById(R.id.act_main_user_name);

        act_main_greetingmsg.setTypeface(mFontContract.NahumSquareR_Regular());
        act_main_user_name.setTypeface(mFontContract.NahumSquareR_Regular());

    }

    @Override
    protected void onStart() {
        super.onStart();

        setGreetingText();
        act_main_user_name.setText(PreferencesManager.getNickname(getApplicationContext()));
        act_main_nav_user_name.setText(mUser.getDisplayName());
        act_main_nav_user_email.setText(mUser.getEmail());

        if(mUser.getPhotoUrl() != null){
            Glide.with(getApplicationContext()).load(mUser.getPhotoUrl().toString())
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(act_main_nav_user_image);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.img_profile_none)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(act_main_nav_user_image);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        menu.findItem(R.id.menu_change_todo_frg).setChecked(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_todo_frg:
                act_main_appbar_folder_floatingbtn.setVisibility(View.VISIBLE);
                if(isFolderFragment){   //심플투두리스트
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out)
                            .hide(mTodoFolderListFragment)
                            .show(mTodoSimpleListFragment)
                            .commit();

                    isFolderFragment = false;
                    act_main_appbar_folder_floatingbtn.setVisibility(View.GONE);
                    item.setIcon(R.drawable.btn_gridview);
                }else{                  //폴더리스트
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out)
                            .hide(mTodoSimpleListFragment)
                            .show(mTodoFolderListFragment)
                            .commit();

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

    private void setGreetingText(){
        String greetingMessage = "";
        int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if( 4 <= presentHour && presentHour <= 11 ){
            greetingMessage += getString(R.string.greetings_morning_1);
        } else if( 12<=presentHour && presentHour <= 18 ){
            greetingMessage += getString(R.string.greetings_afternoon_1);
        }else greetingMessage += getString(R.string.greetings_evening_1);

        act_main_greetingmsg.setText(greetingMessage);
    }

    private void createNotification(){
        PendingIntent buttonIntent = PendingIntent.getActivity(this, 0, new Intent(this, SimpleInputDialog.class), PendingIntent.FLAG_UPDATE_CURRENT);
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

        if (id == R.id.nav_home) {
        // 메인 홈 화면으로 이동
            Intent HomeIntent = new Intent(this, MainActivity.class);
            HomeIntent.putExtra("mainhomefragment","homeintent");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(HomeIntent);
            stackBuilder.startActivities();

        } else if (id == R.id.nav_edit_profile) {
        //프로필 정보 화면으로 이동
            Intent EditProfile  = new Intent(MainActivity.this, NavEditProfileActivity.class);
            startActivity(EditProfile);

        } else if (id == R.id.nav_team) {
        // 팀 기능 화면으로 이동 - 현재 잠금화면으로 이동하게
            Intent TeamIntent = new Intent(MainActivity.this, LockScreenActivity.class);
            startActivity(TeamIntent);

        } else if (id == R.id.nav_setting) {
        // 설정 화면으로 이동
            Intent SettingIntent = new Intent(MainActivity.this, NavSettingActivity.class);
            startActivity(SettingIntent);

        } else if (id == R.id.nav_send_msg) {
        // 의견 보낼 화면 팝업

        }
        act_main_drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
