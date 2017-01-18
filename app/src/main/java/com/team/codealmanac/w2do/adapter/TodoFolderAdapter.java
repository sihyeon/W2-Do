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

public class TodoFolderAdapter extends RecyclerView.Adapter<TodoFolderAdapter.ViewHolder> {
    private ArrayList<TodoFolder> mItemList;
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

    public TodoFolderAdapter(ArrayList<TodoFolder> item) {
        this.mItemList = item;
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


//    // 아이템 추가 시 +1 값 증가
//    private void addItem() {
//        if (labels.size() >= 1) {
//            int lastItem = Integer.parseInt(labels.get(labels.size() - 1));
//            labels.add(String.valueOf(lastItem + 1));
//            notifyItemInserted(labels.size() - 1);
//        } else {
//            labels.add(new String("0"));
//            notifyItemInserted(0);
//        }
//
//    }
//
//    // 폴더 삭제
//    private void removeItem(int position) {
//        labels.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}