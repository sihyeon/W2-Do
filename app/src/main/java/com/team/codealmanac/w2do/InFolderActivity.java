package com.team.codealmanac.w2do;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team.codealmanac.w2do.adapter.InFolderListAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;

import java.util.Calendar;

public class InFolderActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "InFolderActivity";
    private RecyclerView act_infolder_todolist;
    private EditText act_infolder_simpletodo_input_edt;

    private InFolderListAdapter mInFolderListAdapter;
    private Toolbar mToolbar;
    private String mFolderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_folder);
        mFolderName = getIntent().getStringExtra("folderName");
        mToolbar = (Toolbar)findViewById(R.id.act_infolder_toolbar);
        setSupportActionBar(mToolbar);

        act_infolder_simpletodo_input_edt = (EditText)findViewById(R.id.act_infolder_simpletodo_input_edt);
        ImageButton act_infolder_simpletodo_input_btn = (ImageButton)findViewById(R.id.act_infolder_simpletodo_input_btn);
        act_infolder_simpletodo_input_btn.setOnClickListener(this);

        act_infolder_todolist = (RecyclerView)findViewById(R.id.act_infolder_todolist);

        ImageButton backButton = (ImageButton)mToolbar.findViewById(R.id.act_infolder_toolbar_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InFolderActivity.this.finish();
            }
        });
        FontContract font = new FontContract(getAssets());
        TextView act_infolder_toolbar_title = (TextView)mToolbar.findViewById(R.id.act_infolder_toolbar_title);
        act_infolder_toolbar_title.setText(mFolderName);

        act_infolder_simpletodo_input_edt.setTypeface(font.NahumSquareR_Regular());
        act_infolder_toolbar_title.setTypeface(font.NahumSquareB_Regular());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInFolderListAdapter = new InFolderListAdapter(getApplicationContext(), mFolderName);

        act_infolder_todolist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        act_infolder_todolist.setAdapter(mInFolderListAdapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_infolder_simpletodo_input_btn:
                SQLiteManager sqliteManager = SQLiteManager.getInstance(getApplicationContext());
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                long todayTimeInMillis = today.getTimeInMillis();
                Todo todo = new Todo(todayTimeInMillis, act_infolder_simpletodo_input_edt.getText().toString(), mFolderName);
                sqliteManager.addTodo(todo);
                mInFolderListAdapter.updateList();
                act_infolder_simpletodo_input_edt.getText().clear();
                break;
        }
    }
}
