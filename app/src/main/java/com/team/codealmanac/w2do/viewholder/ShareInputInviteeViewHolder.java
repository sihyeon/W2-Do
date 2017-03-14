package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-14.
 */

public class ShareInputInviteeViewHolder extends RecyclerView.ViewHolder {
    public TextView adp_shareinputinvitee_display_text;
    public TextView adp_shareinputinvitee_email_text;
    private View mView;
    public ShareInputInviteeViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        adp_shareinputinvitee_display_text = (TextView)itemView.findViewById(R.id.adp_shareinputinvitee_nickname_text);
        adp_shareinputinvitee_email_text = (TextView)itemView.findViewById(R.id.adp_shareinputinvitee_email_text);
    }

    public void bindOnClickListener(View.OnClickListener onClickListener){
        mView.setOnClickListener(onClickListener);
    }
}
