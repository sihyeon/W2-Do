package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.team.codealmanac.w2do.DetailInputActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.InFolderTodoListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class InFolderListAdapter extends RecyclerView.Adapter<InFolderTodoListViewHolder> {
    private final String TAG = "InFolderListAdapter";
    public boolean isLongClicked = false;
    private ArrayList<Todo> mDataList;
    public ArrayList<Todo> mCheckedList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;

    private InFolderListEventListener inFolderListEventListener;

    private String mFolder;

    public interface InFolderListEventListener{
        void OnMultiItemClick();
        void OnStartMultiClick();
    }

    public void setInFolderListEventListener(InFolderListEventListener listener){
        inFolderListEventListener = listener;
    }

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
        if(mCheckedList == null){
            mCheckedList = new ArrayList<>();
        }
    }

    @Override
    public InFolderTodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_infoder_todo, parent, false);
        return new InFolderTodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InFolderTodoListViewHolder holder, final int position) {
        final Todo model = mDataList.get(position);
        boolean heightChangeFlag = true;
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, mContext.getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.itemView.getLayoutParams());
        params.height = height;

        holder.adp_infoldertodo_selected_layout.setVisibility(View.GONE);
        holder.adp_infoldertodo_endline.setBackgroundColor(model.color);

        if (model.check_state == 1) {
            holder.adp_infoldertodo_checkbox.setChecked(true);
        } else {
            holder.adp_infoldertodo_checkbox.setChecked(false);
        }

        holder.adp_infoldertodo_content.setText(model.content);
        if (model.alarm_date != 0) holder.adp_infoldertodo_alarm_img.setVisibility(View.VISIBLE);

        if (model.memo != null && !model.memo.isEmpty())
            holder.adp_infoldertodo_memo_img.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E) hh:mm a");
        holder.adp_infoldertodo_time_text.setText(format.format(model.end_date));

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(model.start_date);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayTimeInMillis = today.getTimeInMillis() + 1000 * 60 * 60 * 24;

        if (model.end_date < todayTimeInMillis) {
            holder.adp_infoldertodo_time_text.setVisibility(View.GONE);
            heightChangeFlag = false;
        }

        if (model.latitude != 500 && model.longitude != 500) {
            holder.adp_infoldertodo_location_img.setVisibility(View.VISIBLE);
            holder.adp_infoldertodo_location_text.setVisibility(View.VISIBLE);
            holder.adp_infoldertodo_location_text.setText(model.location_name);
            heightChangeFlag = true;
        }

        //어댑터뷰 크기
        if (heightChangeFlag) holder.itemView.setLayoutParams(params);

        final View.OnClickListener viewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLongClicked){
                    //다중선택 아닐때
                    Intent intent = new Intent(mContext, DetailInputActivity.class);
                    intent.putExtra("type", DetailInputActivity.TODOUPDATE);
                    intent.putExtra("data", model);
                    mContext.startActivity(intent);
                } else {
                    //다중선택일때
                    multiClickEvent(holder, position);
                }
            }
        };

        final View.OnClickListener checkboxClickListener = new View.OnClickListener() {
            //체크박스 클릭 시 완료된 할일로 이동되는 리스너
            @Override
            public void onClick(View v) {
                if(!isLongClicked){
                    mSQLiteManager.updateCheckStateInTodo(model._ID);
                    updateList();
                }
            }
        };

        View.OnLongClickListener viewLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isLongClicked){
                    return false;
                } else {
                    isLongClicked = true;
                    multiClickEvent(holder, position);
                    inFolderListEventListener.OnStartMultiClick();
                    return true;
                }
            }
        };

        holder.itemView.setOnClickListener(viewClickListener);
        holder.adp_infoldertodo_checkbox.setOnClickListener(checkboxClickListener);
        holder.itemView.setOnLongClickListener(viewLongClickListener);
        Log.d(TAG, "call onBindViewHolder");

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
        return mDataList.size();
    }

    public int getCheckedItemCount(){
        return mCheckedList.size();
    }

    public void removeItem(int position){
        mSQLiteManager.deleteTodo(mDataList.get(position)._ID);
        updateList();
    }

    private void multiClickEvent(InFolderTodoListViewHolder holder, int position){
        if(holder.isMultiChecked){
            //체크 풀기
            holder.isMultiChecked = false;
            holder.adp_infoldertodo_selected_layout.setVisibility(View.GONE);
            mCheckedList.remove(mDataList.get(position));
        } else {
            //지금 체크됨
            holder.isMultiChecked = true;
            holder.adp_infoldertodo_selected_layout.setVisibility(View.VISIBLE);
            mCheckedList.add(mDataList.get(position));
        }
        inFolderListEventListener.OnMultiItemClick();
    }

    public void moveFolderWithMulti(String newFolder){
        mSQLiteManager.changeFolderWithMulti(toStringFromList(mCheckedList), newFolder);
        updateList();
    }

    public void deleteTodoWithMulti(){
        mSQLiteManager.deleteTodoWithMulti(toStringFromList(mCheckedList));
        updateList();
    }

    private String toStringFromList(ArrayList<Todo> list){
        String temp = "";
        for(int i = 0; i < list.size(); i++){
            if(i == list.size()-1){
                temp += list.get(i)._ID;
            } else {
                temp += list.get(i)._ID + ", ";
            }
        }
        return temp;
    }
}
