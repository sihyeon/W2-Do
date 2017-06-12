package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-06-09.
 */

public class LockTodoViewHolder extends RecyclerView.ViewHolder {
    public TextView adp_lockscreen_todo;
    public View adp_lockscreen_bottom_line;
    public LockTodoViewHolder(View itemView) {
        super(itemView);
        adp_lockscreen_todo = (TextView) itemView.findViewById(R.id.adp_lockscreen_todo_content);
        adp_lockscreen_bottom_line = itemView.findViewById(R.id.adp_lockscreen_bottom_line);
    }
}
