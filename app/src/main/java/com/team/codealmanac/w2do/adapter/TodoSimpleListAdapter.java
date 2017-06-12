package com.team.codealmanac.w2do.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.SimpleTodo;

import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;


import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class TodoSimpleListAdapter extends RecyclerView.Adapter<SimpleTodoViewHolder> implements SQLiteManager.TodoSQLiteEventListener {
    private final String TAG = "TodoSImpleListAdapter";
    private ArrayList<SimpleTodo> mDataList;
    private SQLiteManager mSQLiteManager;
    private Context mContext;

    public TodoSimpleListAdapter(Context context) {
        mContext = context;
        mSQLiteManager = SQLiteManager.getInstance(mContext);
        mDataList = mSQLiteManager.getSimpleTodo();
        if(mDataList == null){
            mDataList = new ArrayList<>();
        }
        mSQLiteManager.setTodoDataListener(this);
    }

    @Override
    public SimpleTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpitem_simpletodo, parent, false);
        return new SimpleTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleTodoViewHolder holder, final int position) {
        holder.adp_simpletoday_content.setText(mDataList.get(position).content);
        holder.adp_simpletoday_checkbox.setChecked(false);

        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick Call");
                mSQLiteManager.updateCheckStateInTodo(mDataList.get(position)._ID);
//                if(mSQLiteManager.updateCheckStateInTodo(mDataList.get(position)._ID)){
////                    Animation goneAnimation = new AlphaAnimation(1, 0);
////                    goneAnimation.setDuration(1000);
////                    goneAnimation.setAnimationListener(new Animation.AnimationListener() {
////                        @Override
////                        public void onAnimationStart(Animation animation) {}
////                        @Override
////                        public void onAnimationEnd(Animation animation) {
////                            holder.mView.setVisibility(View.GONE);
////                        }
////                        @Override
////                        public void onAnimationRepeat(Animation animation) {}
////                    });
////                    holder.mView.startAnimation(goneAnimation);
//                }
            }
        };
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public void OnChangeTodo() {
        mDataList = mSQLiteManager.getSimpleTodo();
        this.notifyDataSetChanged();
    }
}
