package com.team.codealmanac.w2do.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.dialog.DeleteDialogFragment;
import com.team.codealmanac.w2do.dialog.SimpleInputDialogFragment;
import com.team.codealmanac.w2do.models.TodoFolder;
import com.team.codealmanac.w2do.viewholder.TodoFolderViewHolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class FolderListAdapter extends RecyclerView.Adapter<TodoFolderViewHolder> implements SQLiteManager.FolderSQLiteEventListener {
    private final String TAG = "FolderListAdapter";
    private ArrayList<TodoFolder> mDataList;
    private SQLiteManager mSQLiteManager;

    public FolderListAdapter(Context context) {
        mSQLiteManager = SQLiteManager.getInstance(context);
        mDataList = mSQLiteManager.getAllTodoFolder();
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mSQLiteManager.setFolderTodoDataListener(this);
    }

    @Override
    public TodoFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_todofolder, parent, false);
        return new TodoFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoFolderViewHolder holder, int position) {
        final TodoFolder todoFolder = mDataList.get(position);
        final FontContract font = new FontContract(holder.itemView.getContext().getAssets());
        holder.adp_todofolder_name.setText(todoFolder.name);
        holder.adp_todofolder_count.setText(String.valueOf(todoFolder.todo_count));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), InFolderActivity.class);
                intent.putExtra("folderName", holder.adp_todofolder_name.getText());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (todoFolder.name.equals(SQLContract.DEFUALT_FOLDER_NAME)) return false;
                changeFromNomalLayoutToLongClickLayout(holder);
                return true;
            }
        });

        holder.adp_todofolder_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialogFragment.newInstance(DeleteDialogFragment.TYPE_FOLDER, todoFolder.name)
                        .show(((Activity) holder.itemView.getContext()).getFragmentManager(), "delete");
                changeFromLongClickLayoutToNomalLayout(holder);
            }
        });

        holder.adp_todofolder_editname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleInputDialogFragment.newInstance(SimpleInputDialogFragment.TYPE_FOLDER_UPDATE, todoFolder.name)
                        .show(((Activity) holder.itemView.getContext()).getFragmentManager(), "simple_input");
                changeFromLongClickLayoutToNomalLayout(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    private void changeFromLongClickLayoutToNomalLayout(TodoFolderViewHolder holder) {
        holder.adp_todofolder_nomal_layout.setVisibility(View.VISIBLE);
        holder.adp_todofolder_longclick_layout.setVisibility(View.GONE);
    }

    private void changeFromNomalLayoutToLongClickLayout(TodoFolderViewHolder holder) {
        holder.adp_todofolder_nomal_layout.setVisibility(View.GONE);
        holder.adp_todofolder_longclick_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnChangeTodoFolder() {
        mDataList = mSQLiteManager.getAllTodoFolder();
        this.notifyDataSetChanged();
    }
}
