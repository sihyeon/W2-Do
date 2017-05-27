package com.team.codealmanac.w2do.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;

/**
 * Created by Choi Jaeung on 2017-02-17.
 */

public class FolderInputDialogFragment extends DialogFragment implements View.OnClickListener{
    private EditText frag_folderInput_edit;
    private Button frag_folderInput_okbtn;
    private SQLiteManager sqliteManager;
    public FolderInputDialogFragment(){}
    public static FolderInputDialogFragment newInstance(){
        return new FolderInputDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //다이알로그 타이틀 삭제
        if(getDialog().getWindow() != null){
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        View v = inflater.inflate(R.layout.fragment_folderinput_dialog, container);
        sqliteManager = SQLiteManager.getInstance(getDialog().getContext());

        frag_folderInput_edit = (EditText)v.findViewById(R.id.frag_folderInput_edit);
        frag_folderInput_okbtn = (Button)v.findViewById(R.id.frag_folderInput_okbtn);

        frag_folderInput_okbtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_folderInput_okbtn){
            if( frag_folderInput_edit.getText().toString().isEmpty() ){
                frag_folderInput_edit.setError("Required");
                return;
            }
            sqliteManager.addTodoFolder(frag_folderInput_edit.getText().toString());
            this.dismiss();
        }
    }
}
