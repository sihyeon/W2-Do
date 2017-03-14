package com.team.codealmanac.w2do.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-03-14.
 */

public class DetailInputInviteeAdapter extends RecyclerView.Adapter<DetailInputInviteeAdapter.ViewHolder> {
    public ArrayList<String> mItemList;
    public RecyclerView mRecyclerView;
    public DetailInputInviteeAdapter(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        mItemList = new ArrayList<>();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_detailinput_invitee, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.adp_detailinput_invitee_text.setText(mItemList.get(position));
        holder.adp_detailinput_invitee_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                if(mItemList.size() == 0) mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void addItem(String email){
        mItemList.add(email);
        notifyItemInserted(mItemList.size());
    }

    public void removeItem(int count){
        mItemList.remove(count);
        notifyItemRemoved(count);
    }

    public ArrayList getList(){
        return mItemList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView adp_detailinput_invitee_text;
        public ImageButton adp_detailinput_invitee_remove_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            adp_detailinput_invitee_text = (TextView)itemView.findViewById(R.id.adp_detailinput_invitee_text);
            adp_detailinput_invitee_remove_btn = (ImageButton)itemView.findViewById(R.id.adp_detailinput_invitee_remove_btn);
        }
    }
}
