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
import com.team.codealmanac.w2do.models.Todo;

import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;


import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class TodoSimpleListAdapter extends RecyclerView.Adapter<SimpleTodoViewHolder> implements SQLiteManager.TodoSQLiteEventListener {
    private final String TAG = "TodoSImpleListAdapter";
    private ArrayList<SimpleTodo> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;

    public TodoSimpleListAdapter(Context context) {
        mContext = context;
        mSQLiteManager = SQLiteManager.getInstance(mContext);
        mDataList = mSQLiteManager.getSimpleTodo();
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
        mSQLiteManager.setTodoListener(this);
    }

    @Override
    public SimpleTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_simpletoday, parent, false);
        return new SimpleTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleTodoViewHolder holder, int position) {
        holder.adp_simpletoday_content.setText(mDataList.get(position).content);
        holder.adp_simpletoday_checkbox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public void OnAddTodo(Todo todo) {
        mDataList.add(new SimpleTodo(todo._ID, todo.check_state, todo.content));
        this.notifyDataSetChanged();
        Log.d(TAG, "OnAddTodo");
    }
}
