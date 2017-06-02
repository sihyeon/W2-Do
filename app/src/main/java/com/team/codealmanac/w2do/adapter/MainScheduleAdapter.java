package com.team.codealmanac.w2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.viewholder.MainScheduleListViewHolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-06-01.
 */

public class MainScheduleAdapter extends RecyclerView.Adapter<MainScheduleListViewHolder> {
    private ArrayList<MainSchedule> mDataList;
    private SQLiteManager sqliteManager;
    public MainScheduleAdapter(Context context){
        sqliteManager = SQLiteManager.getInstance(context);
        mDataList = sqliteManager.getCheckedMainSchedule();
    }

    @Override
    public MainScheduleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_mainschedule, parent, false);
        return new MainScheduleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainScheduleListViewHolder holder, int position) {
        MainSchedule model = mDataList.get(position);
        if(model.check_state == 0) holder.adp_mainschedule_checkbox.setChecked(false);
        else holder.adp_mainschedule_checkbox.setChecked(true);
        holder.adp_mainschedule_content.setText(model.content);
    }

    @Override
    public int getItemCount() {
        if(mDataList != null) return mDataList.size();
        else return 0;
    }
}
