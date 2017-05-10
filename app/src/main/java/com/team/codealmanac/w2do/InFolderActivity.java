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
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.InFolderTodoListViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InFolderActivity extends AppCompatActivity {
    private DatabaseReference mTodoReference;

    private RecyclerView act_infolder_todolist;
    private FirebaseRecyclerAdapter mInFolderListAdapter;
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

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mTodoReference = FirebaseDatabase.getInstance().getReference().child("todo").child(USER_ID);
        Query TodoQuery = mTodoReference.orderByChild("folder_name").equalTo(mFolderName);
        act_infolder_todolist = (RecyclerView)findViewById(R.id.act_infolder_todolist);

        Log.d(TAG, TodoQuery.getRef().toString() + "userId: " + USER_ID + "folder: (" + mFolderName + ")");
        mInFolderListAdapter = new FirebaseRecyclerAdapter<Todo, InFolderTodoListViewHolder>(Todo.class,
                R.layout.adpitem_infoder_todo, InFolderTodoListViewHolder.class, TodoQuery) {
            @Override
            protected void populateViewHolder(InFolderTodoListViewHolder viewHolder, Todo model, int position) {
//                Log.d(TAG, "model: " + model.content);
                boolean heightChageflag = true;
                final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewHolder.adp_infodertodo_endline.getLayoutParams());
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.height = height;

                viewHolder.adp_infodertodo_endline.setBackgroundColor(model.color);
                viewHolder.adp_infodertodo_checkbox.setChecked(model.check_state);
                viewHolder.adp_infodertodo_content.setText(model.content);
                if(model.alarm_date != 0)   viewHolder.adp_infodertodo_alarm_img.setVisibility(View.VISIBLE);

                if(model.sharing != null && !model.sharing.isEmpty())   viewHolder.adp_infodertodo_invite_img.setVisibility(View.VISIBLE);

                if(model.memo != null && !model.memo.isEmpty())   viewHolder.adp_infodertodo_memo_img.setVisibility(View.VISIBLE);

                SimpleDateFormat format = new SimpleDateFormat("M월 d일(E) hh:mm a");
                viewHolder.adp_infodertodo_time_text.setText(format.format(model.end_date));

                Calendar today = Calendar.getInstance();
                today.setTimeInMillis(model.start_date);
                today.set(Calendar.HOUR, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0);
                long todayTimeInMillis = today.getTimeInMillis() + 1000*60*60*24;

                if (model.end_date < todayTimeInMillis){
                    viewHolder.adp_infodertodo_time_text.setVisibility(View.GONE);
                    heightChageflag = false;
                }

                if(model.latitude != -1 && model.longitude != -1){
                    viewHolder.adp_infodertodo_location_img.setVisibility(View.VISIBLE);
                    viewHolder.adp_infodertodo_location_text.setVisibility(View.VISIBLE);
                    viewHolder.adp_infodertodo_location_text.setText(model.location_name);
                    heightChageflag = true;
                }
                if (heightChageflag) viewHolder.adp_infodertodo_endline.setLayoutParams(params);
            }
        };
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
        if(mInFolderListAdapter != null) mInFolderListAdapter.cleanup();
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
