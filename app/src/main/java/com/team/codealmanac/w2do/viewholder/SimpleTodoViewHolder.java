package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class SimpleTodoViewHolder extends RecyclerView.ViewHolder {
    public CheckBox today_checkbox;
    public TextView today_content;
    public SimpleTodoViewHolder(View itemView) {
        super(itemView);
        today_checkbox = (CheckBox)itemView.findViewById(R.id.adp_simpletoday_checkbox);
        today_content = (TextView)itemView.findViewById(R.id.adp_simpletoday_content);
    }
}
