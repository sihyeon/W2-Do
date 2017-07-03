package com.team.codealmanac.w2do.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.CompleteTabActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.CompleteAdapter;
import com.team.codealmanac.w2do.adapter.InFolderListAdapter;

/**
 * Created by sihyeon on 2017-05-31.
 */

public class CompleteTab_TodoFragment extends android.support.v4.app.Fragment {
    private RecyclerView tabfragment_todo_recyclerview;
    public CompleteAdapter mCompleteAdapter;
    public boolean isLongClicked = false;

    public CompleteTab_TodoFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tabfragment_todo_complete, container, false);
        tabfragment_todo_recyclerview = (RecyclerView)view.findViewById(R.id.tabfragment_todo_recyclerview);
        tabfragment_todo_recyclerview.setHasFixedSize(true);
        tabfragment_todo_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mCompleteAdapter = new CompleteAdapter(getContext(), CompleteAdapter.TYPE_TODO);
        mCompleteAdapter.setOnCompleteAdapterListener((CompleteTabActivity)getContext());
        tabfragment_todo_recyclerview.setAdapter(mCompleteAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(tabfragment_todo_recyclerview);
        return view;
    }

    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                mCompleteAdapter.removeItem(position);
            } else if (direction == ItemTouchHelper.RIGHT){
                mCompleteAdapter.checkCancelItem(position);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                final View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                Paint paint = new Paint();
                if (dX > 0) {
                    //왼쪽에서 오른쪽 스와이프
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.swipe_list_complete_cancel));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_redo_completion);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                } else {
                    //오른쪽에서 왼쪽 스와이프
                    paint.setColor(ContextCompat.getColor(getContext(),R.color.swipe_list_delete));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.icn_delate_completion);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return !isLongClicked;
        }
    };
}
