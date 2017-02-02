package com.team.codealmanac.w2do;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.models.SimpleFolderTodo;
import com.team.codealmanac.w2do.models.Todo;

import java.util.ArrayList;

public class InFolderActivity extends AppCompatActivity {

    private DatabaseReference mTodoReference;
    private Query mTodoQuery;
    private ChildEventListener mTodoListener;

    private RecyclerView mInFolderTodoListView;
    private TextView mFolderNameView;
    private Toolbar mToolbar;

    private String mFolderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_folder);
        Intent intent = getIntent();
        mFolderName = intent.getStringExtra("folderName");
        mToolbar = (Toolbar)findViewById(R.id.act_infolder_toolbar);
        setSupportActionBar(mToolbar);

        mTodoReference = FirebaseDatabase.getInstance().getReference().child("todo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mTodoQuery = mTodoReference.equalTo(mFolderName);
        mInFolderTodoListView = (RecyclerView)findViewById(R.id.act_infolder_todolist);
        mFolderNameView = (TextView)findViewById(R.id.act_infolder_name);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFolderNameView.setText(mFolderName);
        mTodoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) return;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    SimpleFolderTodo item = data.getValue(SimpleFolderTodo.class);
                    item.key = data.getKey();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
