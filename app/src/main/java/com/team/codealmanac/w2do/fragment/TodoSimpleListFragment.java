package com.team.codealmanac.w2do.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.viewholder.SimpleTodoViewHolder;

import java.util.Calendar;


public class TodoSimpleListFragment extends Fragment {
    private final String TAG = "TodoSimpleListFragment";

    private FrameLayout main_schedule_framelayout;
    private RecyclerView today_listview;
    private FirebaseRecyclerAdapter mSimpleTodayAdapter;
    private TextView main_schedule_header_text;
    private TextView main_schedule_sec_header_textview;
    private EditText main_schedule_edittext;
    private Button mschedule_input_btn;
    private TextView today_header_text;
    private TextView frag_todosimple_main_schedule_content;
    private CheckBox frag_main_schedule_checkbox;
    private LinearLayout frag_main_schedule_input_layout;

    private DatabaseReference mMainScheduleReference;
    private ChildEventListener mMainScheduleListener;

    private DatabaseReference mSimpleTodoReference;

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

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mMainScheduleReference = FirebaseDatabase.getInstance().getReference().child("main_schedule").child(userId);
        mSimpleTodoReference = FirebaseDatabase.getInstance().getReference().child("simple_todo").child(userId);

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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Log.d(TAG, "time: " + (calendar.getTimeInMillis() + (1000 * 60 * 60 * 24 - 1000)));

        Query simpleTodoQuery = mSimpleTodoReference.orderByChild("check_state").equalTo(false);
//      Query simpleTodoQuery = mSimpleTodoReference.orderByChild("date").
//                startAt(calendar.getTimeInMillis()).endAt(calendar.getTimeInMillis() + (1000 * 60 * 60 * 24 - 1000));
        today_listview.setLayoutManager(new GridLayoutManager(getContext(), 1));

        mSimpleTodayAdapter = new FirebaseRecyclerAdapter<SimpleTodo, SimpleTodoViewHolder>(SimpleTodo.class,
                R.layout.adpitem_simpletoday, SimpleTodoViewHolder.class, simpleTodoQuery) {
            private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            @Override
            protected void populateViewHolder(SimpleTodoViewHolder viewHolder, SimpleTodo model, int position) {
                viewHolder.mSimpleTodoReference = getRef(position);
                viewHolder.mTodoReference = FirebaseDatabase.getInstance().getReference().child("todo").
                        child(userId).child(getRef(position).getKey());
                viewHolder.adp_simpletoday_content.setText(model.content);
                viewHolder.adp_simpletoday_checkbox.setChecked(model.check_state);
            }
        };
        today_listview.setAdapter(mSimpleTodayAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                main_schedule_header_text.setVisibility(View.VISIBLE);
                frag_todosimple_main_schedule_content.setText(main_schedule);
                writeMainSchedule(main_schedule);
            }
        });

        frag_todosimple_main_schedule_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMainSchedule();
                main_schedule_header_text.setVisibility(View.GONE);
            }
        });

        // Add value event listener to the mainSchedule
        // [START main_schedule_value_event_listener]
        ChildEventListener mainScheduleListener= new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String content = dataSnapshot.getValue().toString();
                main_schedule_sec_header_textview.setText(content);
                getView().findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.GONE);
                getView().findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.VISIBLE);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                main_schedule_sec_header_textview.setText(R.string.frag_maininput_msg);
                getView().findViewById(R.id.frag_todosimple_main_schedule_input_layout).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.frag_todosimple_main_schedule_exist_layout).setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mSimpleTodayAdapter != null){
            mSimpleTodayAdapter.cleanup();
        }
    }
}
