package com.team.codealmanac.w2do;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class InFolderActivity extends AppCompatActivity {

    private RecyclerView mInFolderTodoListView;
    private TextView mFolderNameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_folder);
        mInFolderTodoListView = (RecyclerView)findViewById(R.id.act_infolder_todolist);
        mFolderNameView = (TextView)findViewById(R.id.act_infolder_name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        mFolderNameView.setText(intent.getStringExtra("folderName"));
    }
}
