package com.team.codealmanac.w2do.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.FolderListAdapter;
import com.team.codealmanac.w2do.database.SQLiteManager;

public class TodoFolderListFragment extends Fragment{
    private String TAG = "TodoFolderListFragment";

    private RecyclerView mFolderListView;
    private FolderListAdapter mFolderListAdapter;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //content_main의 recyclerview 설정
        mFolderListView = (RecyclerView) getActivity().findViewById(R.id.frag_todofolder_folderlist);
        mFolderListView.setHasFixedSize(true);
        mFolderListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mFolderListAdapter = new FolderListAdapter(getActivity());
        mFolderListView.setAdapter(mFolderListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
