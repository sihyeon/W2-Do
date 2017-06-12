package com.team.codealmanac.w2do.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.TodoLockScreenAdapter;
import com.team.codealmanac.w2do.database.SQLiteManager;


/**
 * Created by sihyeon on 2017-06-09.
 */

public class TodoLockScreenFragment extends Fragment {
    private final String TAG = "TodoLockScreenFragment";

    private LinearLayout act_lockscreen_layout_todo_layout;
    private RecyclerView act_lockscreen_todo_listview;
    private TodoLockScreenAdapter mLockTodoAdapter;

    private SQLiteManager sqliteManager;


    public TodoLockScreenFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodoLockScreenFragment newInstance() {
        return new TodoLockScreenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "call onCreateView");
        // Inflate the layout for this fragment
        sqliteManager = SQLiteManager.getInstance(getActivity());
        View view = inflater.inflate(R.layout.activity_lock_screen, container, false);
        act_lockscreen_layout_todo_layout = (LinearLayout)view.findViewById(R.id.act_lockscreen_layout_todo_layout);
        act_lockscreen_todo_listview = (RecyclerView)view.findViewById(R.id.act_lockscreen_todo_listview);

        view.findViewById(R.id.act_lockscreen_layout_ignore_mainschedule).setVisibility(View.GONE);
        view.findViewById(R.id.act_lockscreen_layout_exist_mainschedule).setVisibility(View.VISIBLE);
        act_lockscreen_layout_todo_layout.setVisibility(View.VISIBLE);

        mLockTodoAdapter = new TodoLockScreenAdapter(getActivity());

        act_lockscreen_todo_listview.setHasFixedSize(true);
        act_lockscreen_todo_listview.setVisibility(View.VISIBLE);
        act_lockscreen_todo_listview.setAdapter(mLockTodoAdapter);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "call onStart");
    }
}
