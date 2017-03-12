package com.team.codealmanac.w2do.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
    public Button adp_simpletoday_delete_btn;
    public DatabaseReference mSimpleTodoReference;
    public DatabaseReference mTodoReference;
    public SimpleTodoViewHolder(View itemView) {
        super(itemView);
        adp_simpletoday_checkbox = (CheckBox)itemView.findViewById(R.id.adp_simpletoday_checkbox);
        adp_simpletoday_content = (EditText) itemView.findViewById(R.id.adp_simpletoday_content);
        adp_simpletoday_delete_btn = (Button) itemView.findViewById(R.id.adp_simpletoday_delete_btn);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !adp_simpletoday_checkbox.isChecked();
                adp_simpletoday_checkbox.setChecked(isChecked);
                mSimpleTodoReference.child("check_state").setValue(isChecked);
                mTodoReference.child("check_state").setValue(isChecked);
            }
        });
        adp_simpletoday_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSimpleTodoReference.child("visible").setValue(false);
            }
        });

        adp_simpletoday_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){ //b == false
                    adp_simpletoday_content.setFocusable(false);
                    adp_simpletoday_content.setClickable(false);
                    adp_simpletoday_content.setFocusableInTouchMode(false);
                    adp_simpletoday_delete_btn.setVisibility(View.GONE);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("SimpleTodoViewHolder", "long click");
                adp_simpletoday_content.setFocusable(true);
                adp_simpletoday_content.setFocusableInTouchMode(true);
                adp_simpletoday_content.requestFocus(EditText.FOCUS_LEFT);
                adp_simpletoday_delete_btn.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }
}
