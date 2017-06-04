package com.team.codealmanac.w2do.dialog;

import android.app.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;

import java.util.Calendar;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class SimpleInputDialogFragment extends DialogFragment {
    private final String TAG = "SimpleInputDialog";
    private EditText frag_simpleinput_edittext;
    private static final String PARAM = "type";
    private static final String PARAM2 = "folder_name";
    public static final String TYPE_TODO_INPUT = "todo_input";
    public static final String TYPE_FOLDER_INPUT = "folder_input";
    public static final String TYPE_FOLDER_UPDATE = "folder_update";
    private String mType;
    private String mFolderName;

    public SimpleInputDialogFragment() {
    }

    /**
     * @param param type
     */
    public static SimpleInputDialogFragment newInstance(String param) {
        SimpleInputDialogFragment fragment = new SimpleInputDialogFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param param     type
     * @param param2    folder_name
     */
    public static SimpleInputDialogFragment newInstance(String param, String param2) {
        SimpleInputDialogFragment fragment = new SimpleInputDialogFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, param);
        args.putString(PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialogfragment_simpleinput_todo, container);
        mType = getArguments().getString(PARAM);


        TextView frag_simpletodo_header_text = (TextView) v.findViewById(R.id.frag_simpletodo_header_text);
        frag_simpleinput_edittext = (EditText) v.findViewById(R.id.frag_simpleinput_edittext);
        Button frag_simpleinput_cancel_btn = (Button) v.findViewById(R.id.frag_simpleinput_cancel_btn);
        Button frag_simpleinput_submit_btn = (Button) v.findViewById(R.id.frag_simpleinput_submit_btn);

        FontContract mFontContract = new FontContract(getDialog().getContext().getAssets());
        frag_simpleinput_edittext.setTypeface(mFontContract.NahumSquareR_Regular());
        frag_simpletodo_header_text.setTypeface(mFontContract.FranklinGothic_MediumCond());
        frag_simpleinput_cancel_btn.setTypeface(mFontContract.NahumSquareR_Regular());
        frag_simpleinput_submit_btn.setTypeface(mFontContract.NahumSquareR_Regular());


        switch (mType){
            case TYPE_TODO_INPUT:
                frag_simpleinput_submit_btn.setOnClickListener(onTodoInputClickListener);
                frag_simpleinput_cancel_btn.setOnClickListener(onTodoInputClickListener);
                break;

            case TYPE_FOLDER_INPUT:
                frag_simpletodo_header_text.setText("폴더 추가");
                frag_simpleinput_edittext.setHint("폴더명을 입력해주세요.");
                frag_simpleinput_submit_btn.setOnClickListener(onFolderInputClickListener);
                frag_simpleinput_cancel_btn.setOnClickListener(onFolderInputClickListener);
                break;

            case TYPE_FOLDER_UPDATE:
                mFolderName = getArguments().getString(PARAM2);
                frag_simpletodo_header_text.setText("폴더명 변경");
                frag_simpleinput_edittext.setHint("폴더명을 입력해주세요.");
                frag_simpleinput_submit_btn.setOnClickListener(onFolderUpdateClickListener);
                frag_simpleinput_cancel_btn.setOnClickListener(onFolderUpdateClickListener);
                break;
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener onFolderInputClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.frag_simpleinput_submit_btn) {
                if (TextUtils.isEmpty(frag_simpleinput_edittext.getText())) {
                    frag_simpleinput_edittext.setError("내용을 입력해주세요.");
                    return;
                }
                SQLiteManager sqliteManager = SQLiteManager.getInstance(getDialog().getContext());
                sqliteManager.addTodoFolder(frag_simpleinput_edittext.getText().toString());
                dismiss();
            } else {
                dismiss();
            }
        }
    };

    private View.OnClickListener onTodoInputClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.frag_simpleinput_submit_btn) {
                if (TextUtils.isEmpty(frag_simpleinput_edittext.getText())) {
                    frag_simpleinput_edittext.setError("내용을 입력해주세요.");
                    return;
                }
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                long todayTimeInMillis = today.getTimeInMillis();
                Todo todo = new Todo(todayTimeInMillis, frag_simpleinput_edittext.getText().toString());
                SQLiteManager sqliteManager = SQLiteManager.getInstance(getDialog().getContext());
                sqliteManager.addTodo(todo);
                dismiss();
            } else {
                dismiss();
            }
        }
    };

    private View.OnClickListener onFolderUpdateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.frag_simpleinput_submit_btn) {
                if (TextUtils.isEmpty(frag_simpleinput_edittext.getText())) {
                    frag_simpleinput_edittext.setError("내용을 입력해주세요.");
                    return;
                }
                SQLiteManager sqliteManager = SQLiteManager.getInstance(getDialog().getContext());
                sqliteManager.updateTodoFolderOnlyName(mFolderName, frag_simpleinput_edittext.getText().toString());
                dismiss();
            } else {
                dismiss();
            }
        }
    };
}
