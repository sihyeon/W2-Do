package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.CompleteListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.R.attr.type;

/**
 * Created by Choi Jaeung on 2017-06-10.
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteListViewHolder> {
    private final String TAG = CompleteAdapter.class.getSimpleName();
    private ArrayList mDataList;
    public static final String TYPE_TODO = "todo";
    public static final String TYPE_MAINSCHEDULE = "main_schedule";
    private String mType;
    private SQLiteManager sqliteManager;
    public CompleteAdapter(Context context, String type){
        mType = type;
        sqliteManager = SQLiteManager.getInstance(context);
        if(type.equals(TYPE_TODO)){
            mDataList = sqliteManager.getCheckedTodo();
        } else {
            mDataList = sqliteManager.getCheckedMainSchedule();
        }
    }

    @Override
    public CompleteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_completelist, parent, false);
        return new CompleteListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteListViewHolder holder, int position) {
        Object model = mDataList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        Log.d(TAG, mType);
        if(mType.equals(TYPE_TODO)){
            Log.d(TAG, ((Todo)model).content);
            holder.adp_completelist_content.setText(((Todo)model).content);
            holder.adp_completelist_checked_date.setText(format.format(((Todo)model).check_date));
            holder.adp_completelist_endline.setVisibility(View.VISIBLE);
            holder.adp_completelist_endline.setBackgroundColor(((Todo)model).color);
        } else if(mType.equals(TYPE_MAINSCHEDULE)){
            holder.adp_completelist_content.setText(((MainSchedule)model).content);
            holder.adp_completelist_checked_date.setText(format.format(((MainSchedule)model).check_date));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
