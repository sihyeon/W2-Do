package com.team.codealmanac.w2do.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.SimpleToday;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-01-18.
 */

public class SimpleTodayAdapter extends RecyclerView.Adapter<SimpleTodayAdapter.ViewHolder> {
    private ArrayList<SimpleToday> mItemList;

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox today_checkbox;
        TextView today_content;
        ViewHolder(View itemView) {
            super(itemView);
            today_checkbox = (CheckBox)itemView.findViewById(R.id.adp_simpletoday_checkbox);
            today_content = (TextView)itemView.findViewById(R.id.adp_simpletoday_content);
        }
    }

    public SimpleTodayAdapter(ArrayList<SimpleToday> mItemList){
        this.mItemList = mItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_simpletoday_item, null);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleToday todayItem = mItemList.get(position);
        holder.today_checkbox.setActivated(todayItem.check_state);
        holder.today_content.setText(todayItem.content);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


}
