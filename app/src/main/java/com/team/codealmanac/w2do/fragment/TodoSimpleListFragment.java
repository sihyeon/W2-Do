package com.team.codealmanac.w2do.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.TodoSimpleListAdapter;


public class TodoSimpleListFragment extends Fragment {
    private final String TAG = "TodoSimpleListFragment";

    private FrameLayout main_schedule_framelayout;
    private RecyclerView today_listview;
    private TodoSimpleListAdapter mSimpleTodoAdapter;
    private TextView main_schedule_header_text;
    private TextView main_schedule_sec_header_textview;
    private EditText main_schedule_edittext;
    private Button mschedule_input_btn;
    private TextView today_header_text;
    private TextView frag_todosimple_main_schedule_content;
    private CheckBox frag_main_schedule_checkbox;
    private LinearLayout frag_main_schedule_input_layout;

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
        View view = inflater.inflate(R.layout.fragment_todo_simple_list, container, false);

        main_schedule_framelayout = (FrameLayout)view.findViewById(R.id.main_schedule_framelayout);
        frag_main_schedule_input_layout = (LinearLayout)view.findViewById(R.id.frag_main_schedule_input_layout);
        main_schedule_header_text = (TextView)view.findViewById(R.id.frag_main_schedule_header_text);
        main_schedule_sec_header_textview = (TextView)view.findViewById(R.id.frag_maininput_msg);
        main_schedule_edittext = (EditText)view.findViewById(R.id.main_schedule_input);
        frag_todosimple_main_schedule_content = (TextView) view.findViewById(R.id.frag_todosimple_main_schedule_content);
        frag_main_schedule_checkbox = (CheckBox)view.findViewById(R.id.frag_main_schedule_checkbox);

        mschedule_input_btn = (Button)view.findViewById(R.id.mainschedule_input_btn);
        today_header_text = (TextView)view.findViewById(R.id.act_simpleinput_header_text);
        today_listview = (RecyclerView)view.findViewById(R.id.frag_today_listview);

        Typeface main_schedule_font = Typeface.createFromAsset(getActivity().getAssets(), "FranklinGothicMediumCond.TTF");
        main_schedule_header_text.setTypeface(main_schedule_font);
        today_header_text.setTypeface(main_schedule_font);

        Typeface main_schedule_Kfont = Typeface.createFromAsset(getActivity().getAssets(), "NanumSquareB.ttf");
        main_schedule_sec_header_textview.setTypeface(main_schedule_Kfont);
        main_schedule_edittext.setTypeface(main_schedule_Kfont);
        frag_todosimple_main_schedule_content.setTypeface(main_schedule_Kfont);

        view.findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.GONE);

        today_listview.setHasFixedSize(true);
        today_listview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mSimpleTodoAdapter = new TodoSimpleListAdapter(getActivity());
        today_listview.setAdapter(mSimpleTodoAdapter);
        return view;
    }

    // TODO: 2017-05-25 메인스케줄 디비 및 추가 제거 생성 필요

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
//
//        mschedule_input_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String main_schedule = main_schedule_edittext.getText().toString();
//                if(TextUtils.isEmpty(main_schedule)){
//                    main_schedule_edittext.setError("메인 스케줄을 입력해주세요.");
//                    return;
//                }
//                main_schedule_header_text.setVisibility(View.VISIBLE);
//                frag_todosimple_main_schedule_content.setText(main_schedule);
//                writeMainSchedule(main_schedule);
//            }
//        });
//
//        frag_todosimple_main_schedule_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                removeMainSchedule();
//                main_schedule_header_text.setVisibility(View.GONE);
//            }
//        });
    }
}
