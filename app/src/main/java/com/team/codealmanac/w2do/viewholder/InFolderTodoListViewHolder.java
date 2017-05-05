package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-10.
 */

public class InFolderTodoListViewHolder extends RecyclerView.ViewHolder{
    public CheckBox adp_infodertodo_checkbox;
    public TextView adp_infodertodolist_content;
    public ImageView adp_infodertodo_alarm_img;
    public ImageView adp_infodertodo_invite_img;
    public ImageView adp_infodertodo_memo_img;
    public TextView adp_infodertodo_time_text;
    public ImageView adp_infodertodo_location_img;
    public TextView adp_infodertodo_location_text;
    public InFolderTodoListViewHolder(View itemView) {
        super(itemView);
        adp_infodertodo_checkbox = (CheckBox)itemView.findViewById(R.id.adp_infodertodo_checkbox);
        adp_infodertodolist_content = (TextView)itemView.findViewById(R.id.adp_infodertodo_content);
        adp_infodertodo_alarm_img = (ImageView)itemView.findViewById(R.id.adp_infodertodo_alarm_img);
        adp_infodertodo_invite_img = (ImageView)itemView.findViewById(R.id.adp_infodertodo_invite_img);
        adp_infodertodo_memo_img = (ImageView)itemView.findViewById(R.id.adp_infodertodo_memo_img);
        adp_infodertodo_time_text = (TextView)itemView.findViewById(R.id.adp_infodertodo_time_text);
        adp_infodertodo_location_img = (ImageView)itemView.findViewById(R.id.adp_infodertodo_location_img);
        adp_infodertodo_location_text = (TextView) itemView.findViewById(R.id.adp_infodertodo_location_text);
    }
}
