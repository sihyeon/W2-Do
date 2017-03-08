package com.team.codealmanac.w2do.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.listeners.RecyclerViewOnItemClickListener;
import com.team.codealmanac.w2do.models.TodoFolder;
import com.team.codealmanac.w2do.viewholder.TodoFolderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TodoFolderListFragment extends Fragment {
    private String TAG = "TodoFolderListFragment";

    private RecyclerView mFolderListView;
    private DatabaseReference mTodoFolderReference;

    public TodoFolderListFragment() {
        // Required empty public constructor
    }

    public static TodoFolderListFragment newInstance() {
        return new TodoFolderListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_folder_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mTodoFolderReference = FirebaseDatabase.getInstance().getReference().child("todo_folder")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("folder");

        // content_main의 recyclerview 설정
        mFolderListView = (RecyclerView) getActivity().findViewById(R.id.frag_todofolder_folderlist);
        mFolderListView.setHasFixedSize(true);
        mFolderListView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mFolderListView.setAdapter(new FirebaseRecyclerAdapter<TodoFolder, TodoFolderViewHolder>(TodoFolder.class, R.layout.adpitem_todofolder_item, TodoFolderViewHolder.class, mTodoFolderReference) {
            @Override
            protected void populateViewHolder(TodoFolderViewHolder viewHolder, TodoFolder model, int position) {
                viewHolder.folder_name.setText(model.name);
                viewHolder.todo_count.setText(String.valueOf(model.todo_count));
            }
        });
    }
}
