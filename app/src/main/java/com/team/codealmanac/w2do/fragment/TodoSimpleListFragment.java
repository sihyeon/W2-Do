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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

    private LinearLayout frag_todosimple_main_schedule_input_layout;
    private TextView frag_simpletodo_maininput_msg;
    private EditText frag_simpletodo_main_schedule_input;
    private Button frag_simpetodo_main_schedule_input_btn;

    private LinearLayout frag_todosimple_main_schedule_exist_layout;
    private TextView frag_simpletodo_main_schedule_header_text;
    private TextView frag_simpletodo_main_schedule_content;
    private CheckBox frag_simpletodo_main_schedule_checkbox;

    private TextView frag_simpletodo_header_text;
    private RecyclerView frag_simpletodo_listview;
    private TodoSimpleListAdapter mSimpleTodoAdapter;

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
        View view = inflater.inflate(R.layout.fragment_simpletodo_list, container, false);

        //메인스케줄 입력부분
        frag_todosimple_main_schedule_input_layout = (LinearLayout) view.findViewById(R.id.frag_todosimple_main_schedule_input_layout);
        frag_simpletodo_maininput_msg = (TextView) view.findViewById(R.id.frag_simpletodo_maininput_msg);
        frag_simpletodo_main_schedule_input = (EditText) view.findViewById(R.id.frag_simpletodo_main_schedule_input);
        frag_simpetodo_main_schedule_input_btn = (Button) view.findViewById(R.id.frag_simpetodo_main_schedule_input_btn);
//        Animation visibleAnimation = new AlphaAnimation(0, 1);
//        visibleAnimation.setDuration(1000);

        //메인스케줄 출력부분
        frag_todosimple_main_schedule_exist_layout = (LinearLayout) view.findViewById(R.id.frag_todosimple_main_schedule_exist_layout);
        frag_simpletodo_main_schedule_header_text = (TextView) view.findViewById(R.id.frag_simpletodo_main_schedule_header_text);
        frag_simpletodo_main_schedule_content = (TextView) view.findViewById(R.id.frag_simpletodo_main_schedule_content);
        frag_simpletodo_main_schedule_checkbox = (CheckBox) view.findViewById(R.id.frag_simpletodo_main_schedule_checkbox);

        frag_simpletodo_header_text = (TextView) view.findViewById(R.id.frag_simpletodo_header_text);
        frag_simpletodo_listview = (RecyclerView) view.findViewById(R.id.frag_simpletodo_listview);

        Typeface main_schedule_font = Typeface.createFromAsset(getActivity().getAssets(), "FranklinGothicMediumCond.TTF");
        frag_simpletodo_main_schedule_header_text.setTypeface(main_schedule_font);
        frag_simpletodo_header_text.setTypeface(main_schedule_font);

        Typeface main_schedule_Kfont = Typeface.createFromAsset(getActivity().getAssets(), "NanumSquareB.ttf");
        frag_simpletodo_maininput_msg.setTypeface(main_schedule_Kfont);
        frag_simpletodo_main_schedule_input.setTypeface(main_schedule_Kfont);
        frag_simpletodo_main_schedule_content.setTypeface(main_schedule_Kfont);

        view.findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.GONE);

        frag_simpletodo_listview.setHasFixedSize(true);
        frag_simpletodo_listview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mSimpleTodoAdapter = new TodoSimpleListAdapter(getActivity());
        frag_simpletodo_listview.setAdapter(mSimpleTodoAdapter);
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
            frag_simpletodo_main_schedule_header_text.setVisibility(View.VISIBLE);
            frag_simpletodo_main_schedule_content.setText(mMainSchedule.content);
            frag_todosimple_main_schedule_input_layout.setVisibility(View.GONE);
            frag_todosimple_main_schedule_exist_layout.setVisibility(View.VISIBLE);
        } else {
            Animation goneAnimation = new AlphaAnimation(1, 0);
            goneAnimation.setDuration(1000*1);
            goneAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    frag_todosimple_main_schedule_input_layout.setVisibility(View.VISIBLE);
                    frag_todosimple_main_schedule_exist_layout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            frag_todosimple_main_schedule_exist_layout.startAnimation(goneAnimation);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkExistMainSchedule();

        frag_simpetodo_main_schedule_input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String main_schedule = frag_simpletodo_main_schedule_input.getText().toString();
                if (TextUtils.isEmpty(main_schedule)) {
                    frag_simpletodo_main_schedule_input.setError("메인 스케줄을 입력해주세요.");
                    return;
                }
                if (sqliteManager.setMainSchedule(frag_simpletodo_main_schedule_input.getText().toString())) {
                    checkExistMainSchedule();
                    frag_simpletodo_main_schedule_input.getText().clear();
                    frag_simpletodo_main_schedule_checkbox.setChecked(false);
                }
            }
        });

        frag_simpletodo_main_schedule_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqliteManager.updateCheckInMainSchedule(mMainSchedule._ID);
                checkExistMainSchedule();
            }
        });
    }
}
