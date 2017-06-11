package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.CompleteListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Choi Jaeung on 2017-06-10.
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteListViewHolder> {
    private final String TAG = CompleteAdapter.class.getSimpleName();
    private ArrayList mDataList;
    private ArrayList mCheckedList;
    public static final String TYPE_TODO = "todo";
    public static final String TYPE_MAINSCHEDULE = "main_schedule";
    private boolean isLongClicked = false;
    private String mType;
    private SQLiteManager sqliteManager;

    private CompleteAdapterListener listener;
    public interface CompleteAdapterListener{
        void onItemLongClick(String type);
        void onMultiClick(int count);
    }

    public void setOnCompleteAdapterListener(CompleteAdapterListener listener){
        this.listener = listener;
    }

    public CompleteAdapter(Context context, String type){
        mType = type;
        sqliteManager = SQLiteManager.getInstance(context);
        if(type.equals(TYPE_TODO)){
            mDataList = sqliteManager.getCheckedTodo();
        } else {
            mDataList = sqliteManager.getCheckedMainSchedule();
        }
        mCheckedList = new ArrayList();
    }

    @Override
    public CompleteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_completelist, parent, false);
        return new CompleteListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompleteListViewHolder holder, final int position) {
        Object model = mDataList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        holder.adp_completelist_selected_layout.setVisibility(View.GONE);
        holder.isMultiChecked = false;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLongClicked) {
                    multiClickEvent(holder, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isLongClicked){
                    return false;
                }
                isLongClicked = true;
                listener.onItemLongClick(mType);
                multiClickEvent(holder, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void multiClickEvent(CompleteListViewHolder holder, int position){
        if(holder.isMultiChecked){
            //체크 풀기
            holder.isMultiChecked = false;
            holder.adp_completelist_selected_layout.setVisibility(View.GONE);
            mCheckedList.remove(mDataList.get(position));
        } else {
            //지금 체크됨
            holder.isMultiChecked = true;
            holder.adp_completelist_selected_layout.setVisibility(View.VISIBLE);
            mCheckedList.add(mDataList.get(position));
        }
        listener.onMultiClick(mCheckedList.size());
    }

    private String toStringFromList(ArrayList list){
        String temp = "";
        for(int i = 0; i < list.size(); i++){
            if(i == list.size()-1){
                temp += mType.equals(TYPE_TODO)? ((Todo)list.get(i))._ID: ((MainSchedule)list.get(i))._ID;
            } else {
                temp += mType.equals(TYPE_TODO)? ((Todo)list.get(i))._ID: ((MainSchedule)list.get(i))._ID + ", ";
            }
        }
        return temp;
    }

    public void deleteData(){
        if(mType.equals(TYPE_TODO)){
            sqliteManager.deleteTodoWithMulti(toStringFromList(mCheckedList));
        } else {
            sqliteManager.deleteMainScheduleWithMulti(toStringFromList(mCheckedList));
        }
        updateList();
    }

    public void updateList(){
        isLongClicked = false;
        mCheckedList.clear();
        if(mType.equals(TYPE_TODO)){
            mDataList = sqliteManager.getCheckedTodo();
        } else {
            mDataList = sqliteManager.getCheckedMainSchedule();
        }
        notifyDataSetChanged();
    }
}
