package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.viewholder.InFolderTodoListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class InFolderListAdapter extends RecyclerView.Adapter<InFolderTodoListViewHolder>{
    private final String TAG = "InFolderListAdapter";
    private ArrayList<Todo> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;

    public InFolderListAdapter(Context context, String folder) {
        mContext = context;
        mSQLiteManager = SQLiteManager.getInstance(mContext);
        if(folder.equals(SQLContract.DEFUALT_FOLDER_NAME)){
            mDataList = mSQLiteManager.getAllTodoList();
        } else {
            mDataList = mSQLiteManager.getTodoListInFolder(folder);
        }
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
    }

    @Override
    public InFolderTodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_infoder_todo, parent, false);
        return new InFolderTodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InFolderTodoListViewHolder holder, int position) {
        Todo model = mDataList.get(position);
        boolean heightChangeFlag = true;
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, mContext.getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.adp_infodertodo_endline.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.height = height;

        holder.adp_infodertodo_endline.setBackgroundColor(model.color);
        if(model.check_state == 1) holder.adp_infodertodo_checkbox.setChecked(true);

        holder.adp_infodertodo_content.setText(model.content);
        if (model.alarm_date != 0) holder.adp_infodertodo_alarm_img.setVisibility(View.VISIBLE);

        if (model.memo != null && !model.memo.isEmpty())
            holder.adp_infodertodo_memo_img.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E) hh:mm a");
        holder.adp_infodertodo_time_text.setText(format.format(model.end_date));

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(model.start_date);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayTimeInMillis = today.getTimeInMillis() + 1000 * 60 * 60 * 24;

        if (model.end_date < todayTimeInMillis) {
            holder.adp_infodertodo_time_text.setVisibility(View.GONE);
            heightChangeFlag = false;
        }
        Log.d(TAG, "lat: " + model.latitude + "lon: " + model.longitude);
        if (model.latitude != -1 && model.longitude != -1) {
            holder.adp_infodertodo_location_img.setVisibility(View.VISIBLE);
            holder.adp_infodertodo_location_text.setVisibility(View.VISIBLE);
            holder.adp_infodertodo_location_text.setText(model.location_name);
            heightChangeFlag = true;
        }
        if (heightChangeFlag) holder.adp_infodertodo_endline.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }
}
