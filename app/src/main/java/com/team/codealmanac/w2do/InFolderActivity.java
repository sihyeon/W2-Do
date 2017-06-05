package com.team.codealmanac.w2do;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team.codealmanac.w2do.adapter.InFolderListAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.id.list;

public class InFolderActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "InFolderActivity";
    private RecyclerView act_infolder_todolist;
    private EditText act_infolder_simpletodo_input_edt;

    private InFolderListAdapter mInFolderListAdapter;
    private Toolbar mToolbar;
    private String mFolderName;
    private ArrayList<Todo> mDataList;
    private int edit_position;
    private Paint p = new Paint();
    private View view;

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
        TextView titleText = (TextView)mToolbar.findViewById(R.id.act_infolder_toolbar_title);
        titleText.setText(mFolderName);
        titleText.setTypeface(font.NahumSquareB_Regular());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInFolderListAdapter = new InFolderListAdapter(getApplicationContext(), mFolderName);
        act_infolder_todolist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        act_infolder_todolist.setAdapter(mInFolderListAdapter);
        act_infolder_todolist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        act_infolder_todolist.setLayoutManager(layoutManager);
        initSwipe();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    mInFolderListAdapter.removeItem(position);
                } else {
                    removeView();
                    edit_position = position;
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    final View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        //왼쪽에서 오른쪽 스와이프
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_alarm);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        //오른쪽에서 왼쪽 스와이프
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_logo);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(act_infolder_todolist);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
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