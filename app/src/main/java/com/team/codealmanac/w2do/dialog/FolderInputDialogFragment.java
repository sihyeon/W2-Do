package com.team.codealmanac.w2do.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.TodoFolder;

/**
 * Created by Choi Jaeung on 2017-02-17.
 */

public class FolderInputDialogFragment extends DialogFragment implements View.OnClickListener{
    public  FolderInputDialogFragment(){}
    private EditText frag_folderInput_edit;
    private Button frag_folderInput_okbtn;
    private DatabaseReference mTodoFolderReference;
    public static FolderInputDialogFragment newInstance(){
        return new FolderInputDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //다이알로그 타이틀 삭제
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_folderinput_dialog, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTodoFolderReference = FirebaseDatabase.getInstance().getReference().child("todo_folder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        frag_folderInput_edit = (EditText)getDialog().findViewById(R.id.frag_folderInput_edit);
        frag_folderInput_okbtn = (Button)getDialog().findViewById(R.id.frag_folderInput_okbtn);

        frag_folderInput_okbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_folderInput_okbtn){
            if( frag_folderInput_edit.getText().toString().isEmpty() ){
                frag_folderInput_edit.setError("Required");
                return;
            }
            final String key = mTodoFolderReference.push().getKey();
            mTodoFolderReference.child("folder_count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTodoFolderReference.child(key).setValue(new TodoFolder((long)dataSnapshot.getValue(), frag_folderInput_edit.getText().toString(), 0));
                    mTodoFolderReference.child("folder_count").setValue( ((long)dataSnapshot.getValue()+1) );
                    getDialog().dismiss();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
    }
}
