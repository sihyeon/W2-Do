package com.team.codealmanac.w2do.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-01-23.
 */

public class InFolderTodoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_infodertodolist, null);
        return (new InFolderTodoAdapter.ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
