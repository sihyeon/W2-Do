package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.MainSchedule;

import java.util.Calendar;


public class TodoSimpleListFragment extends Fragment {
    private final String TAG = "TodoSimpleListFragment";

    private OnFragmentInteractionListener mListener;

    private FrameLayout main_schedule_framelayout;
    private RecyclerView today_listview;
    private TextView main_schedule_header;
    private TextView main_schedule_sec_header;
    private EditText main_schedule_edittext;
    private Button mschedule_input_btn;

    private String mUserId;
    private DatabaseReference mMainScheduleReference;
    private ChildEventListener mMainScheduleListener;
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
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // [START initialize_database_ref]
        mMainScheduleReference = FirebaseDatabase.getInstance().getReference().child("main_schedule").child(mUserId);
        // [END initialize_database_ref]
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

        main_schedule_framelayout.setVisibility(View.VISIBLE);
        main_schedule_header.setVisibility(View.VISIBLE);
        main_schedule_sec_header.setVisibility(View.VISIBLE);
        main_schedule_edittext.setVisibility(View.VISIBLE);
        today_listview.setVisibility(View.VISIBLE);
        mschedule_input_btn.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("온뷰크리에이티드", view.toString());
    }

    @Override
    public void onStart(){
        super.onStart();
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

        main_schedule_sec_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMainSchedule();
            }
        });

        // Add value event listener to the mainSchedule
        // [START main_schedule_value_event_listener]
        ChildEventListener mainScheduleListener= new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String content = dataSnapshot.getValue().toString();
                main_schedule_sec_header.setText(content);
                getView().findViewById(R.id.main_schedule_input_layout).setVisibility(View.GONE);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                main_schedule_sec_header.setText(R.string.frag_maininput_msg);
                getView().findViewById(R.id.main_schedule_input_layout).setVisibility(View.VISIBLE);
                main_schedule_edittext.setText(null);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mMainScheduleReference.child("visible").addChildEventListener(mainScheduleListener);
        // [END main_schedule_value_event_listener]
        // Keep copy of mainSchedule listener so we can remove it when app stops
        mMainScheduleListener = mainScheduleListener;

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mMainScheduleListener != null){
            mMainScheduleReference.child("visible").removeEventListener(mMainScheduleListener);
        }
    }

    private void removeMainSchedule(){
        mMainScheduleReference.child("visible").removeValue();
    }

    // 메인 스케줄 데이터 write 함수
    private void writeMainSchedule(String main_schedule){
        Calendar rightNow = Calendar.getInstance();
        String Year = String.valueOf(rightNow.get(Calendar.YEAR));
        String month = String.valueOf(rightNow.get(Calendar.MONTH) + 1);
        long date = rightNow.getTimeInMillis();

        String key = mMainScheduleReference.child(Year).child(month).push().getKey();

        MainSchedule mainScheduleModel = new MainSchedule(main_schedule, date);
        mMainScheduleReference.child(Year).child(month).child(key).setValue(mainScheduleModel);

        mMainScheduleReference.child("visible").setValue(mainScheduleModel.toVisibleMainSchedule());
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
