package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.AddRemoveNumberedAdapter;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_folder_list, container, false);
        // content_main의 recyclerview 설정
        recyclerView = (RecyclerView) view.findViewById(R.id.app2_recyclerView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setBackgroundColor(Color.parseColor("#30000000"));
        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4));
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
