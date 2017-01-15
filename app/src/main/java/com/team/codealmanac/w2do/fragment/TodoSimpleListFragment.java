package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.AddRemoveNumberedAdapter;
import com.team.codealmanac.w2do.models.MainSchedule;


public class TodoSimpleListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FrameLayout main_schedule_framelayout;
    private RecyclerView today_listview;
    private TextView main_schedule_header;
    private TextView main_schedule_sec_header;
    private EditText main_schedule_edittext;
    private Button mschedule_input_btn;
    private DatabaseReference mDatabase;

    public TodoSimpleListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodoSimpleListFragment newInstance() {
        TodoSimpleListFragment fragment = new TodoSimpleListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_simple_list, container, false);

        main_schedule_framelayout = (FrameLayout)view.findViewById(R.id.main_schedule_framelayout);
        main_schedule_header = (TextView)view.findViewById(R.id.frag_main_schedule_header_text);
        main_schedule_sec_header = (TextView)view.findViewById(R.id.frag_maininput_msg);
        main_schedule_edittext = (EditText)view.findViewById(R.id.main_schedule_input);
        mschedule_input_btn = (Button)view.findViewById(R.id.mainschedule_input_btn);
        today_listview = (RecyclerView)view.findViewById(R.id.today_listview);

        main_schedule_header.setVisibility(View.VISIBLE);
        main_schedule_sec_header.setVisibility(View.VISIBLE);
        main_schedule_edittext.setVisibility(View.VISIBLE);
        today_listview.setVisibility(View.VISIBLE);
        mschedule_input_btn.setVisibility(View.VISIBLE);
        mschedule_input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String main_schedule = main_schedule_edittext.getText().toString();
                if(TextUtils.isEmpty(main_schedule)){
                    main_schedule_edittext.setError("메인 스케줄을 입력해주세요.");
                    return;
                }
                // 메인 스케줄 db 연결하세요~
                writeMainSchedule(main_schedule);
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    // 메인 스케줄 데이터 write 함수
    private void writeMainSchedule(String mainschedule){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
