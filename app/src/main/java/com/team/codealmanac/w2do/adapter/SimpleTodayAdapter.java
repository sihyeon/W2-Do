package com.team.codealmanac.w2do.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.SimpleTodo;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-01-18.
 */

public class SimpleTodayAdapter extends FirebaseRecyclerAdapter<SimpleTodo, SimpleTodoViewHolder> {
    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public SimpleTodayAdapter(Class<SimpleTodo> modelClass, int modelLayout, Class<SimpleTodoViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(SimpleTodoViewHolder viewHolder, SimpleTodo model, int position) {
        viewHolder.today_content.setText(model.content);
        viewHolder.today_checkbox.setActivated(model.visible);
    }
}
