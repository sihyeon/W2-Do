package com.team.codealmanac.w2do.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.TodoFolder;
import com.team.codealmanac.w2do.viewholder.TodoFolderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TodoFolderAdapter extends FirebaseRecyclerAdapter<TodoFolder, TodoFolderViewHolder> {

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
    public TodoFolderAdapter(Class<TodoFolder> modelClass, int modelLayout, Class<TodoFolderViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(TodoFolderViewHolder viewHolder, TodoFolder model, int position) {
        viewHolder.folder_name.setText(model.name);
        viewHolder.todo_count.setText(String.valueOf(model.todo_count));
    }
}