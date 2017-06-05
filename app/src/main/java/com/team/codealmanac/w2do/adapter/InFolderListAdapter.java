package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.DetailInputActivity;
import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.InFolderTodoListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class InFolderListAdapter extends RecyclerView.Adapter<InFolderTodoListViewHolder> {
    private final String TAG = "InFolderListAdapter";
    private ArrayList<Todo> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;
    private String mFolder;

    public InFolderListAdapter(Context context, String folder) {
        mContext = context;
        mSQLiteManager = SQLiteManager.getInstance(mContext);
        mFolder = folder;

        if(folder == null){
            mDataList = mSQLiteManager.getCheckedTodo();
        } else {
            mDataList = mSQLiteManager.getTodoListInFolder(folder);
        }
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
    }

    @Override
    public InFolderTodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_infoder_todo, parent, false);
        return new InFolderTodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InFolderTodoListViewHolder holder, int position) {

        final Todo model = mDataList.get(position);

        boolean heightChangeFlag = true;
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, mContext.getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.adp_infodertodo_endline.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.height = height;


        holder.adp_infodertodo_endline.setBackgroundColor(model.color);
        if (model.check_state == 1) {
            holder.adp_infodertodo_checkbox.setChecked(true);
        } else {
            holder.adp_infodertodo_checkbox.setChecked(false);
        }


        holder.adp_infodertodo_content.setText(model.content);
        if (model.alarm_date != 0) holder.adp_infodertodo_alarm_img.setVisibility(View.VISIBLE);

        if (model.memo != null && !model.memo.isEmpty())
            holder.adp_infodertodo_memo_img.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E) hh:mm a");
        holder.adp_infodertodo_time_text.setText(format.format(model.end_date));

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(model.start_date);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayTimeInMillis = today.getTimeInMillis() + 1000 * 60 * 60 * 24;

        if (model.end_date < todayTimeInMillis) {
            holder.adp_infodertodo_time_text.setVisibility(View.GONE);
            heightChangeFlag = false;
        }
        Log.d(TAG, "lat: " + model.latitude + "lon: " + model.longitude);
        if (model.latitude != 500 && model.longitude != 500) {
            holder.adp_infodertodo_location_img.setVisibility(View.VISIBLE);
            holder.adp_infodertodo_location_text.setVisibility(View.VISIBLE);
            holder.adp_infodertodo_location_text.setText(model.location_name);
            heightChangeFlag = true;
        }
        if (heightChangeFlag) holder.adp_infodertodo_endline.setLayoutParams(params);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailInputActivity.class);
                intent.putExtra("type", DetailInputActivity.TODOUPDATE);
                intent.putExtra("data", model);
                mContext.startActivity(intent);
            }
        });

        holder.adp_infodertodo_checkbox.setOnClickListener(new View.OnClickListener() {
            //체크박스 클릭 시 완료된 할일로 이동되는 리스너
            @Override
            public void onClick(View v) {
                mSQLiteManager.updateCheckStateInTodo(model._ID);
                mDataList = mSQLiteManager.getTodoListInFolder(model.folder_name);
                InFolderListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public void updateList(){
        if(mFolder == null){
            mDataList = mSQLiteManager.getCheckedTodo();
        } else {
            mDataList = mSQLiteManager.getTodoListInFolder(mFolder);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    public void removeItem(int position){
        mSQLiteManager.deleteTodo(mDataList.get(position)._ID);
        mDataList = mSQLiteManager.getTodoListInFolder(mFolder);
        this.notifyDataSetChanged();
    }

}
