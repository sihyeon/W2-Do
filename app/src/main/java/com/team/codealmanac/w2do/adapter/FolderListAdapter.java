package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.TodoFolder;
import com.team.codealmanac.w2do.viewholder.TodoFolderViewHolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class FolderListAdapter extends RecyclerView.Adapter<TodoFolderViewHolder> implements SQLiteManager.FolderSQLiteEventListener{
    private final String TAG = "FolderListAdapter";
    private ArrayList<TodoFolder> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;
    public FolderListAdapter(Context context){
        mContext = context;
        mSQLiteManager = new SQLiteManager(mContext);
        mDataList = mSQLiteManager.getAllTodoFolder();
        mSQLiteManager.setFolderTodoListener(this);
    }

    @Override
    public TodoFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_todofolder, parent, false);
        return new TodoFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoFolderViewHolder holder, int position) {
        holder.folder_name.setText(mDataList.get(position).name);
        holder.todo_count.setText( String.valueOf(mDataList.get(position).todo_count) );
    }

    @Override
    public int getItemCount() {
        if(mDataList != null){
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public void OnAddTodoFolder(int position) {
        mDataList = mSQLiteManager.getAllTodoFolder();
        Log.d(TAG, "list size: "+mDataList.size());
        this.notifyDataSetChanged();
    }
}
