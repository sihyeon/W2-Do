package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-03-08.
 */

public class SimpleTodoViewHolder extends RecyclerView.ViewHolder {
    public CheckBox today_checkbox;
    public TextView today_content;
    public DatabaseReference mSimpleTodoReference;
    public DatabaseReference mTodoReference;
    public SimpleTodoViewHolder(View itemView) {
        super(itemView);
        today_checkbox = (CheckBox)itemView.findViewById(R.id.adp_simpletoday_checkbox);
        today_content = (TextView)itemView.findViewById(R.id.adp_simpletoday_content);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !today_checkbox.isChecked();
                today_checkbox.setChecked(isChecked);
                mSimpleTodoReference.child("check_state").setValue(isChecked);
                mTodoReference.child("check_state").setValue(isChecked);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }
}
