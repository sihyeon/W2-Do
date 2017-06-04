package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-05-31.
 */

public class MainScheduleListViewHolder extends RecyclerView.ViewHolder {
    public CheckBox adp_mainschedule_checkbox;
    public TextView adp_mainschedule_content;
    public MainScheduleListViewHolder(View itemView) {
        super(itemView);
        adp_mainschedule_checkbox = (CheckBox)itemView.findViewById(R.id.adp_mainschedule_checkbox);
        adp_mainschedule_content = (TextView)itemView.findViewById(R.id.adp_mainschedule_content);
    }
}
