package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.InFolderActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.TodoFolderAdapter;
import com.team.codealmanac.w2do.listeners.RecyclerViewOnItemClickListener;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodoFolderListFragment extends Fragment {
    private String TAG = "TodoFolderListFragment";

    private RecyclerView mFolderListView;
    private DatabaseReference mTodoFolderReference;
    private ChildEventListener mTodoFolderListener;

    private TodoFolderAdapter mFolderListAdapter;

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
        final List<TodoFolder> TodoFolderItemList = new ArrayList<>();

        final RecyclerView.SimpleOnItemTouchListener folderItemListener = new RecyclerViewOnItemClickListener(getContext(),
                mFolderListView, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String folderName = ((TextView)v.findViewById(R.id.adp_todofolder_name)).getText().toString();
                Intent intent = new Intent(TodoFolderListFragment.this.getContext(), InFolderActivity.class);
                intent.putExtra("folderName", folderName);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View v, int position) {}
        });

        mFolderListView.setHasFixedSize(true);
        mFolderListView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mFolderListView.addOnItemTouchListener(folderItemListener);
        mFolderListAdapter = new TodoFolderAdapter(TodoFolderItemList);
        mFolderListView.setAdapter(mFolderListAdapter);

        mTodoFolderListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey() + " - " + dataSnapshot.getValue());
                mFolderListAdapter.addItem(dataSnapshot.getValue(TodoFolder.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + dataSnapshot.getValue());
                mFolderListAdapter.changeItem(dataSnapshot.getValue(TodoFolder.class));

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: " + dataSnapshot.getKey() + " - " + dataSnapshot.getValue());
                mFolderListAdapter.removeItem((int)dataSnapshot.getValue(TodoFolder.class).sequence);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mTodoFolderReference.orderByValue().addChildEventListener(mTodoFolderListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTodoFolderListener != null) mTodoFolderReference.removeEventListener(mTodoFolderListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
