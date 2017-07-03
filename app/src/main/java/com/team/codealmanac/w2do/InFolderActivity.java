package com.team.codealmanac.w2do;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.adapter.InFolderListAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.dialog.DeleteDialogFragment;
import com.team.codealmanac.w2do.dialog.MoveFolderDialogFragment;
import com.team.codealmanac.w2do.models.Todo;

import java.util.Calendar;

public class InFolderActivity extends AppCompatActivity
        implements View.OnClickListener, InFolderListAdapter.InFolderListEventListener, MoveFolderDialogFragment.MoveFolderDialongListener, DeleteDialogFragment.DeleteDialogListener{
    private final String TAG = "InFolderActivity";
    private RecyclerView act_infolder_todolist;
    private EditText act_infolder_simpletodo_input_edt;

    private ImageButton act_infolder_toolbar_back_btn;
    private TextView act_infolder_toolbar_title;
    private RelativeLayout act_infolder_toolbar_longclick_layout;
    private TextView act_infolder_toolbar_longclick_count;

    private InFolderListAdapter mInFolderListAdapter;
    private Toolbar mToolbar;
    private String mFolderName;
    private Paint mPaint = new Paint();
    private ItemTouchHelper mItemTouchHelper;

    private boolean isLongClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_folder);
        mFolderName = getIntent().getStringExtra("folderName");
        mToolbar = (Toolbar) findViewById(R.id.act_infolder_toolbar);
        setSupportActionBar(mToolbar);

        act_infolder_simpletodo_input_edt = (EditText) findViewById(R.id.act_infolder_simpletodo_input_edt);
        ImageButton act_infolder_simpletodo_input_btn = (ImageButton) findViewById(R.id.act_infolder_simpletodo_input_btn);

        act_infolder_toolbar_longclick_layout = (RelativeLayout) findViewById(R.id.act_infolder_toolbar_longclick_layout);
        act_infolder_toolbar_longclick_count = (TextView) findViewById(R.id.act_infolder_toolbar_longclick_count);
        ImageButton act_infolder_toolbar_longclick_share = (ImageButton) findViewById(R.id.act_infolder_toolbar_longclick_share);
        ImageButton act_infolder_toolbar_longclick_movefolder = (ImageButton) findViewById(R.id.act_infolder_toolbar_longclick_movefolder);
        ImageButton act_infolder_toolbar_longclick_delete = (ImageButton) findViewById(R.id.act_infolder_toolbar_longclick_delete);

        act_infolder_simpletodo_input_btn.setOnClickListener(this);
        act_infolder_toolbar_longclick_share.setOnClickListener(this);
        act_infolder_toolbar_longclick_movefolder.setOnClickListener(this);
        act_infolder_toolbar_longclick_delete.setOnClickListener(this);

        act_infolder_todolist = (RecyclerView) findViewById(R.id.act_infolder_todolist);

        act_infolder_toolbar_back_btn = (ImageButton) mToolbar.findViewById(R.id.act_infolder_toolbar_back_btn);
        act_infolder_toolbar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InFolderActivity.this.finish();
            }
        });
        FontContract font = new FontContract(getAssets());
        act_infolder_toolbar_title = (TextView) mToolbar.findViewById(R.id.act_infolder_toolbar_title);
        act_infolder_toolbar_title.setText(mFolderName);

        act_infolder_simpletodo_input_edt.setTypeface(font.NahumSquareR_Regular());
        act_infolder_toolbar_title.setTypeface(font.NahumSquareB_Regular());
        act_infolder_toolbar_longclick_count.setTypeface(font.NahumSquareB_Regular());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInFolderListAdapter = new InFolderListAdapter(getApplicationContext(), mFolderName);
        act_infolder_todolist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        act_infolder_todolist.setAdapter(mInFolderListAdapter);
        act_infolder_todolist.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        act_infolder_todolist.setLayoutManager(layoutManager);
        mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(act_infolder_todolist);

        mInFolderListAdapter.setInFolderListEventListener(this);
    }

    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                mInFolderListAdapter.removeItem(position);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                final View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                if (dX > 0) {
                    //왼쪽에서 오른쪽 스와이프
                    mPaint.setColor(Color.parseColor("#388E3C"));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    c.drawRect(background, mPaint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_alarm);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, mPaint);
                } else {
                    //오른쪽에서 왼쪽 스와이프
                    mPaint.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, mPaint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_logo);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, mPaint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return isLongClicked;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_infolder_simpletodo_input_btn:
                if (TextUtils.isEmpty(act_infolder_simpletodo_input_edt.getText())) {
                    act_infolder_simpletodo_input_edt.setError("입력된 할일이 없습니다.");
                    return;
                }
                SQLiteManager sqliteManager = SQLiteManager.getInstance(getApplicationContext());
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                long todayTimeInMillis = today.getTimeInMillis();

                Todo todo = new Todo(todayTimeInMillis, act_infolder_simpletodo_input_edt.getText().toString(), mFolderName);
                sqliteManager.addTodo(todo);
                mInFolderListAdapter.updateList();
                act_infolder_simpletodo_input_edt.setText(null);
                break;
            case R.id.act_infolder_toolbar_longclick_share:
                mInFolderListAdapter.sharedTodoWithMulti();
                cancelMultiClick();
                break;
            case R.id.act_infolder_toolbar_longclick_movefolder:
                MoveFolderDialogFragment.newInstance().show(getFragmentManager(), "move_folder");
                break;
            case R.id.act_infolder_toolbar_longclick_delete:
                DeleteDialogFragment.newInstance(DeleteDialogFragment.TYPE_TODO, null).show(getFragmentManager(), "delete_todo");
                break;
        }
    }

    private void cancelMultiClick(){
        mInFolderListAdapter.isLongClicked = false;
        act_infolder_toolbar_title.setVisibility(View.VISIBLE);
        act_infolder_toolbar_longclick_layout.setVisibility(View.GONE);
        mInFolderListAdapter.mCheckedList.clear();
        act_infolder_toolbar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InFolderActivity.this.finish();
            }
        });
        mInFolderListAdapter.notifyDataSetChanged();
    }

    //MoveFolderDialogFragment interface
    @Override
    public void OnMoveButtonClickListener(String newFolder) {
        mInFolderListAdapter.moveFolderWithMulti(newFolder);
        cancelMultiClick();
    }

    //DeleteDialogFramgent interface
    @Override
    public void OnDelete() {
        mInFolderListAdapter.deleteTodoWithMulti();
        cancelMultiClick();
    }

    // [START InFolderListAdapter interface]
    @Override
    public void OnMultiItemClick() {
        act_infolder_toolbar_longclick_count.setText(mInFolderListAdapter.getCheckedItemCount() + "개");
    }
    @Override
    public void OnStartMultiClick() {
        act_infolder_toolbar_title.setVisibility(View.GONE);
        act_infolder_toolbar_longclick_layout.setVisibility(View.VISIBLE);
        isLongClicked = false;
        act_infolder_toolbar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelMultiClick();
            }
        });
    }
    // [END InFolderListAdapter]


}