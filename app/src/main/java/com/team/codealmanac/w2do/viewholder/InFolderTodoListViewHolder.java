package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-10.
 */

public class InFolderTodoListViewHolder extends RecyclerView.ViewHolder{
    public TextView adp_infodertodolist_sequence;
    public TextView adp_infodertodolist_content;
    public InFolderTodoListViewHolder(View itemView) {
        super(itemView);
        adp_infodertodolist_sequence = (TextView)itemView.findViewById(R.id.adp_infodertodo_sequence);
        adp_infodertodolist_content = (TextView)itemView.findViewById(R.id.adp_infodertodo_content);
    }
}
