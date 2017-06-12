package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.viewholder.LockTodoViewHolder;
import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;

import java.util.ArrayList;

/**
 * Created by sihyeon on 2017-06-09.
 */

public class TodoLockScreenAdapter extends RecyclerView.Adapter<LockTodoViewHolder> implements SQLiteManager.TodoSQLiteEventListener {
    private final String TAG = "LockTodoAdapter";
    private ArrayList<SimpleTodo> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;

    public TodoLockScreenAdapter(Context context) {
        mContext = context;
        mSQLiteManager = SQLiteManager.getInstance(mContext);
        mDataList = mSQLiteManager.getSimpleTodo();
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
        mSQLiteManager.setTodoDataListener(this);
    }

    @Override
    public LockTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_lockscreen_todo, parent, false);
        return new LockTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LockTodoViewHolder holder, final int position) {
        holder.adp_lockscreen_todo.setText(mDataList.get(position).content);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public void OnChangeTodo() {
        mDataList = mSQLiteManager.getSimpleTodo();
        this.notifyDataSetChanged();
    }
}
