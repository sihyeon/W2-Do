package com.team.codealmanac.w2do.dialog;

import android.content.Context;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.User;
import com.team.codealmanac.w2do.viewholder.ShareInputInviteeViewHolder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnShareInputDialogListener} interface
 * to handle interaction events.
 * Use the {@link ShareInputDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareInputDialogFragment extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String ARG_PARAM = "param";
    private CharSequence[] mParam;
    private OnShareInputDialogListener mListener;
    private EditText frag_shareInput_edt;
    private ImageButton frag_shareInput_search_btn;
    private RecyclerView frag_shareInput_recyclerview;
    private DatabaseReference mPublicUsersReference;
    private Query mQuery;
    private FirebaseRecyclerAdapter mAdapter;

    public ShareInputDialogFragment() {}
    // TODO: Rename and change types and number of parameters
    public static ShareInputDialogFragment newInstance(String[] param) {
        ShareInputDialogFragment fragment = new ShareInputDialogFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArray(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getCharSequenceArray(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mPublicUsersReference = FirebaseDatabase.getInstance().getReference().child("public_users");
        return inflater.inflate(R.layout.fragment_share_input_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        frag_shareInput_edt = (EditText)getDialog().findViewById(R.id.frag_shareInput_edt);
        frag_shareInput_search_btn = (ImageButton)getDialog().findViewById(R.id.frag_shareInput_search_btn);
        frag_shareInput_recyclerview = (RecyclerView)getDialog().findViewById(R.id.frag_shareInput_recyclerview);
        frag_shareInput_edt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        frag_shareInput_edt.setOnEditorActionListener(this);
        frag_shareInput_search_btn.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager)getDialog().getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShareInputDialogListener) {
            mListener = (OnShareInputDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShareInputDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(mAdapter != null) mAdapter.cleanup();
    }

    @Override
    public void onClick(View view) {
        if(mAdapter != null) mAdapter.cleanup();
        mQuery = mPublicUsersReference.orderByChild("email").startAt(frag_shareInput_edt.getText().toString());
        frag_shareInput_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mAdapter = new FirebaseRecyclerAdapter<User, ShareInputInviteeViewHolder>(User.class, R.layout.adpitem_share_input_invitee,
                ShareInputInviteeViewHolder.class, mQuery) {
            @Override
            protected void populateViewHolder(ShareInputInviteeViewHolder viewHolder, final User model, int position) {
                viewHolder.adp_shareinputinvitee_display_text.setText(model.display_name);
                viewHolder.adp_shareinputinvitee_email_text.setText(model.email);
                viewHolder.bindOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onInviteeClick(model.email);
                        InputMethodManager imm = (InputMethodManager)getDialog().getContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(frag_shareInput_edt.getWindowToken(), 0);
                        getDialog().dismiss();
                    }
                });
            }
        };
        frag_shareInput_recyclerview.setAdapter(mAdapter);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            onClick(getView());
            InputMethodManager imm = (InputMethodManager)getDialog().getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(frag_shareInput_edt.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    public interface OnShareInputDialogListener {
        // TODO: Update argument type and name
        void onInviteeClick(String email);
    }
}
