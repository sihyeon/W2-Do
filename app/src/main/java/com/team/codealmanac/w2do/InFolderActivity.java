package com.team.codealmanac.w2do;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.team.codealmanac.w2do.adapter.InFolderListAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.InFolderTodoListViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InFolderActivity extends AppCompatActivity {
    private DatabaseReference mTodoReference;

    private RecyclerView act_infolder_todolist;
    private InFolderListAdapter mInFolderListAdapter;
    private Toolbar mToolbar;

    private String mFolderName;

    private String USER_ID;
    private final String TAG = "InFolderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_folder);
        mFolderName = getIntent().getStringExtra("folderName");
        mToolbar = (Toolbar)findViewById(R.id.act_infolder_toolbar);
        setSupportActionBar(mToolbar);

        act_infolder_todolist = (RecyclerView)findViewById(R.id.act_infolder_todolist);
        mInFolderListAdapter = new InFolderListAdapter(getApplicationContext(), mFolderName);

        act_infolder_todolist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        act_infolder_todolist.setAdapter(mInFolderListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton backButton = (ImageButton)mToolbar.findViewById(R.id.act_infolder_toolbar_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InFolderActivity.this.finish();
            }
        });
        FontContract font = new FontContract(getAssets());
        TextView titleText = (TextView)mToolbar.findViewById(R.id.act_infolder_toolbar_title);
        titleText.setText(mFolderName);
        titleText.setTypeface(font.NahumSquareB_Regular());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
