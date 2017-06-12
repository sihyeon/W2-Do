package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class SimpleTodoViewHolder extends RecyclerView.ViewHolder {
    public CheckBox adp_simpletoday_checkbox;
    public EditText adp_simpletoday_content;

    public SimpleTodoViewHolder(View itemView) {
        super(itemView);
        adp_simpletoday_checkbox = (CheckBox)itemView.findViewById(R.id.adp_simpletoday_checkbox);
        adp_simpletoday_content = (EditText) itemView.findViewById(R.id.adp_simpletoday_content);
    }
}
