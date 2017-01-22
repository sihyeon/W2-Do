package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.TodoFolderAdapter;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoFolderListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoFolderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFolderListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private DatabaseReference mTodoFolderReference;

    private ArrayList<TodoFolder> mTodoFolderList;

    public TodoFolderListFragment() {
        // Required empty public constructor
    }

    public static TodoFolderListFragment newInstance() {
        TodoFolderListFragment fragment = new TodoFolderListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTodoFolderReference = FirebaseDatabase.getInstance().getReference().child("todo_folder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_folder_list, container, false);
        // content_main의 recyclerview 설정
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_todofolder_folderlist);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //데이터 한번 가져옴
        mTodoFolderReference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTodoFolderList = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for (DataSnapshot todoSnapshot : dataSnapshot.getChildren()){
                        mTodoFolderList.add(todoSnapshot.getValue(TodoFolder.class));
                    }
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                recyclerView.setBackgroundColor(Color.parseColor("#30000000"));
                recyclerView.setAdapter(new TodoFolderAdapter(mTodoFolderList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void writeMainSchedule(){
        for(int i = 1; i < 4; i++){
            String key = mTodoFolderReference.push().getKey();

            TodoFolder todoFolderModel = new TodoFolder(i, "테스트"+i, i+3);
            mTodoFolderReference.child(key).setValue(todoFolderModel);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
