package com.team.codealmanac.w2do.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class TodoFolderViewHolder extends RecyclerView.ViewHolder {
    public TextView folder_name;
    public TextView todo_count;

    public TodoFolderViewHolder(View itemView) {
        super(itemView);
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "NanumSquareR.ttf");
        folder_name = (TextView) itemView.findViewById(R.id.adp_todofolder_name);
        todo_count = (TextView) itemView.findViewById(R.id.adp_todofolder_count);
        folder_name.setTypeface(typeface);
        todo_count.setTypeface(typeface);
    }
}

