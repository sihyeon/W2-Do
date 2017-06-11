package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;

/**
 * Created by Choi Jaeung on 2017-06-10.
 */

public class CompleteListViewHolder extends RecyclerView.ViewHolder {
    public TextView adp_completelist_content;
    public TextView adp_completelist_checked_date;
    public View adp_completelist_endline;
    public boolean isMultiChecked = false;
    public RelativeLayout adp_completelist_selected_layout;
    public CompleteListViewHolder(View itemView) {
        super(itemView);
        adp_completelist_content = (TextView)itemView.findViewById(R.id.adp_completelist_content);
        adp_completelist_checked_date = (TextView)itemView.findViewById(R.id.adp_completelist_checked_date);
        adp_completelist_endline = itemView.findViewById(R.id.adp_completelist_endline);
        adp_completelist_selected_layout = (RelativeLayout)itemView.findViewById(R.id.adp_completelist_selected_layout);

        FontContract font = new FontContract(itemView.getContext().getAssets());
        adp_completelist_content.setTypeface(font.NahumSquareR_Regular());
        adp_completelist_checked_date.setTypeface(font.RobotoLight());
    }
}
