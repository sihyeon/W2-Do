package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;

/**
 * Created by Choi Jaeung on 2017-03-10.
 */

public class InFolderTodoListViewHolder extends RecyclerView.ViewHolder{
    public RelativeLayout adp_infoldertodo_selected_layout;
    public RelativeLayout adp_infoldertodo_endline;
    public CheckBox adp_infoldertodo_checkbox;
    public TextView adp_infoldertodo_content;
    public ImageView adp_infoldertodo_alarm_img;
    public ImageView adp_infoldertodo_memo_img;
    public TextView adp_infoldertodo_time_text;
    public ImageView adp_infoldertodo_location_img;
    public TextView adp_infoldertodo_location_text;
    public boolean isMultiChecked = false;
    public InFolderTodoListViewHolder(View itemView) {
        super(itemView);
        FontContract font = new FontContract(itemView.getContext().getAssets());

        adp_infoldertodo_checkbox = (CheckBox)itemView.findViewById(R.id.adp_infoldertodo_checkbox);
        adp_infoldertodo_content = (TextView)itemView.findViewById(R.id.adp_infoldertodo_content);
        adp_infoldertodo_alarm_img = (ImageView)itemView.findViewById(R.id.adp_infoldertodo_alarm_img);
        adp_infoldertodo_memo_img = (ImageView)itemView.findViewById(R.id.adp_infoldertodo_memo_img);
        adp_infoldertodo_time_text = (TextView)itemView.findViewById(R.id.adp_infoldertodo_time_text);
        adp_infoldertodo_location_img = (ImageView)itemView.findViewById(R.id.adp_infoldertodo_location_img);
        adp_infoldertodo_location_text = (TextView) itemView.findViewById(R.id.adp_infoldertodo_location_text);

        adp_infoldertodo_endline = (RelativeLayout)itemView.findViewById(R.id.adp_infoldertodo_endline);
        adp_infoldertodo_selected_layout = (RelativeLayout) itemView.findViewById(R.id.adp_infoldertodo_selected_layout);

        adp_infoldertodo_content.setTypeface(font.NahumSquareR_Regular());
        adp_infoldertodo_time_text.setTypeface(font.NahumSquareR_Regular());
        adp_infoldertodo_location_text.setTypeface(font.NahumSquareR_Regular());
    }
}
