package com.team.codealmanac.w2do;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.codealmanac.w2do.adapter.ListViewAdapter;


public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        mContext = getApplicationContext();


        //APP2 메인 화면 리사이클러뷰 어댑터 생성
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setBackgroundColor(Color.parseColor("#30000000"));
//        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4));

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();


        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                parent.getAdapter().getItem(position);
                if( id == 0 ) {
                    createDesktopIcon();
                    Toast.makeText(SettingActivity.this, "홈 화면에 What 2 do를 추가했습니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, 1);
                }else if(id == 1){
                    startActivity(new Intent(SettingActivity.this, UseMainActivity.class));
                } else if(id == 2){
                    startActivity(new Intent(SettingActivity.this, UseMainActivity.class));
                } else if ( id == 3 ) {
                    Uri uri = Uri.parse("http://sihyun2139.wixsite.com/codealmanac");
                    Intent web = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(web);
                } else {

                }
            }
        });


//        adapter.ArwItem(ContextCompat.getDrawable(this, R.drawable.btn_set_go));
//        adapter.ArwItem(ContextCompat.getDrawable(this, R.drawable.btn_set_go));
//        adapter.ArwItem(ContextCompat.getDrawable(this, R.drawable.btn_set_go));
//        adapter.ArwItem(ContextCompat.getDrawable(this,R.drawable.btn_set_go));

        ImageView header_icn = (ImageView) findViewById(R.id.header_icn);
        TextView header_text = (TextView) findViewById(R.id.header_text);
        TextView copyright = (TextView) findViewById(R.id.copyrignt);
        header_icn.setBackgroundResource(R.drawable.icn_logo);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "GOTHIC.TTF");
        header_text.setTypeface(typeface);
        copyright.setTypeface(typeface);
        copyright.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            startService(new Intent(this, W2DoService.class));
            Toast.makeText(SettingActivity.this, "What 2 do를 실행합니다.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            stopService(new Intent(this, W2DoService.class));
            Toast.makeText(SettingActivity.this, "What 2 do를 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public static Context getContext() {
        return mContext;
    }

    // 바탕화면에 바로가기 아이콘 생성
    public void createDesktopIcon() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("check", "");

        if(pref.getString("check", "").isEmpty()){
            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutIntent.setClassName(this, getClass().getName());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name)); //앱 이름
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.launcher)); //앱 아이콘
            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            sendBroadcast(intent);
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("check", "exist");
        editor.commit();
    }
}
