package com.team.codealmanac.w2do.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.TodoSimpleListAdapter;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.MainSchedule;


public class TodoSimpleListFragment extends Fragment {
    private final String TAG = "TodoSimpleListFragment";

    private RecyclerView frag_today_listview;
    private TodoSimpleListAdapter mSimpleTodoAdapter;
    private TextView frag_main_schedule_header_text;
    private TextView frag_maininput_msg;
    private EditText frag_main_schedule_input;
    private Button frag_main_schedule_input_btn;
    private TextView frag_simpleinput_header_text;
    private TextView frag_todosimple_main_schedule_content;
    private CheckBox frag_main_schedule_checkbox;
    private LinearLayout frag_main_schedule_input_layout;
    private SQLiteManager sqliteManager;
    private MainSchedule mMainSchedule;

    public TodoSimpleListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodoSimpleListFragment newInstance() {
        return new TodoSimpleListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sqliteManager = SQLiteManager.getInstance(getActivity());
        View view = inflater.inflate(R.layout.fragment_todo_simple_list, container, false);

        frag_main_schedule_input_layout = (LinearLayout) view.findViewById(R.id.frag_main_schedule_input_layout);
        frag_main_schedule_header_text = (TextView) view.findViewById(R.id.frag_main_schedule_header_text);
        frag_maininput_msg = (TextView) view.findViewById(R.id.frag_maininput_msg);

        frag_main_schedule_input = (EditText) view.findViewById(R.id.frag_main_schedule_input);
        frag_main_schedule_input_btn = (Button) view.findViewById(R.id.frag_main_schedule_input_btn);

        frag_todosimple_main_schedule_content = (TextView) view.findViewById(R.id.frag_todosimple_main_schedule_content);
        frag_main_schedule_checkbox = (CheckBox) view.findViewById(R.id.frag_main_schedule_checkbox);


        frag_simpleinput_header_text = (TextView) view.findViewById(R.id.frag_simpleinput_header_text);
        frag_today_listview = (RecyclerView) view.findViewById(R.id.frag_today_listview);

        Typeface main_schedule_font = Typeface.createFromAsset(getActivity().getAssets(), "FranklinGothicMediumCond.TTF");
        frag_main_schedule_header_text.setTypeface(main_schedule_font);
        frag_simpleinput_header_text.setTypeface(main_schedule_font);

        Typeface main_schedule_Kfont = Typeface.createFromAsset(getActivity().getAssets(), "NanumSquareB.ttf");
        frag_maininput_msg.setTypeface(main_schedule_Kfont);
        frag_main_schedule_input.setTypeface(main_schedule_Kfont);
        frag_todosimple_main_schedule_content.setTypeface(main_schedule_Kfont);

        view.findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.GONE);

        frag_today_listview.setHasFixedSize(true);
        frag_today_listview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mSimpleTodoAdapter = new TodoSimpleListAdapter(getActivity());
        frag_today_listview.setAdapter(mSimpleTodoAdapter);
        return view;
    }

    // TODO: 2017-05-25 메인스케줄 디비 및 추가 제거 생성 필요

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void checkExistMainSchedule() {
        mMainSchedule = sqliteManager.getMainSchedule();
        if (mMainSchedule != null) {
            frag_main_schedule_header_text.setVisibility(View.VISIBLE);
            frag_todosimple_main_schedule_content.setText(mMainSchedule.content);
            getActivity().findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.GONE);
            getActivity().findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkExistMainSchedule();

        frag_main_schedule_input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String main_schedule = frag_main_schedule_input.getText().toString();
                if (TextUtils.isEmpty(main_schedule)) {
                    frag_main_schedule_input.setError("메인 스케줄을 입력해주세요.");
                    return;
                }
                if (sqliteManager.setMainSchedule(frag_main_schedule_input.getText().toString())) {
                    checkExistMainSchedule();
                    frag_main_schedule_input.getText().clear();
                    frag_main_schedule_checkbox.setChecked(false);
                }
            }
        });

        frag_main_schedule_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqliteManager.updateCheckInMainSchedule(mMainSchedule._ID);
                checkExistMainSchedule();
            }
        });
    }
}
