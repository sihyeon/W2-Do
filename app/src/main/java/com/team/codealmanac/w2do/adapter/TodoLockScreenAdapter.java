package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.viewholder.LockTodoViewHolder;
import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;

import java.util.ArrayList;

/**
 * Created by sihyeon on 2017-06-09.
 */

public class TodoLockScreenAdapter extends RecyclerView.Adapter<LockTodoViewHolder>{
    private final String TAG = "LockTodoAdapter";
    private ArrayList<SimpleTodo> mDataList;
    private SQLiteManager mSQLiteManager;

    public TodoLockScreenAdapter(Context context) {
        mSQLiteManager = SQLiteManager.getInstance(context);
        mDataList = mSQLiteManager.getSimpleTodo();
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
    }

    @Override
    public LockTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_lockscreen_todo, parent, false);
        return new LockTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LockTodoViewHolder holder, final int position) {
        holder.adp_lockscreen_bottom_line.setVisibility(View.VISIBLE);
        if(position+1 == getItemCount()){
            holder.adp_lockscreen_bottom_line.setVisibility(View.GONE);
        }
        holder.adp_lockscreen_todo.setText(mDataList.get(position).content);
        FontContract font = new FontContract(holder.itemView.getContext().getAssets());
        holder.adp_lockscreen_todo.setTypeface(font.NahumSquareR_Regular());
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }
}
