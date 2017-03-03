package com.team.codealmanac.w2do.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.util.ArrayList;
import java.util.List;

public class TodoFolderAdapter extends RecyclerView.Adapter<TodoFolderAdapter.ViewHolder> {
    private List<TodoFolder> mItemList;
    class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView folder_name;
        protected TextView todo_count;

        ViewHolder(View itemView) {
            super(itemView);
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "NanumSquareR.ttf");
            folder_name = (TextView)itemView.findViewById(R.id.adp_todofolder_name);
            todo_count = (TextView)itemView.findViewById(R.id.adp_todofolder_count);
            folder_name.setTypeface(typeface); todo_count.setTypeface(typeface);
        }
    }

    public TodoFolderAdapter(List<TodoFolder> itemList) {
        this.mItemList = itemList;
    }

    public void addItem(TodoFolder item){
        mItemList.add(item);
        notifyItemInserted(getItemCount());
    }

    public void changeItem(TodoFolder item){
        mItemList.set((int)item.sequence, item);
        notifyItemChanged((int)item.sequence);
    }

    public void removeItem(int position){
        mItemList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_todofolder_item, null);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoFolder todoFolderItem = mItemList.get(position);
        holder.folder_name.setText(todoFolderItem.name);
        holder.todo_count.setText(String.valueOf(todoFolderItem.todo_count));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}