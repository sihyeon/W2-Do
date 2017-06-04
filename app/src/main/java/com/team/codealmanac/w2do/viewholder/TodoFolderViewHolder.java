package com.team.codealmanac.w2do.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class TodoFolderViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout adp_todofolder_nomal_layout;
    public TextView adp_todofolder_name;
    public TextView adp_todofolder_count;
    public LinearLayout adp_todofolder_longclick_layout;
    public ImageButton adp_todofolder_delete_btn;
    public ImageButton adp_todofolder_editname_btn;
    public TodoFolderViewHolder(final View itemView) {
        super(itemView);
        FontContract fontContract = new FontContract(itemView.getContext().getAssets());
        adp_todofolder_nomal_layout = (RelativeLayout) itemView.findViewById(R.id.adp_todofolder_nomal_layout);
        adp_todofolder_name = (TextView) itemView.findViewById(R.id.adp_todofolder_name);
        adp_todofolder_count = (TextView) itemView.findViewById(R.id.adp_todofolder_count);

        adp_todofolder_longclick_layout = (LinearLayout) itemView.findViewById(R.id.adp_todofolder_longclick_layout);
        adp_todofolder_delete_btn = (ImageButton) itemView.findViewById(R.id.adp_todofolder_delete_btn);
        adp_todofolder_editname_btn = (ImageButton) itemView.findViewById(R.id.adp_todofolder_editname_btn);

        adp_todofolder_name.setTypeface(fontContract.NahumSquareR_Regular());
        adp_todofolder_count.setTypeface(fontContract.NahumSquareR_Regular());
    }
}

