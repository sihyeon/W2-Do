package com.team.codealmanac.w2do.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.models.Todo;

import java.util.Calendar;

/**
 * Created by sihyeon on 2017-01-15.
 */

public class DeleteDialogFragment extends DialogFragment {
    private final String TAG = "DeleteDialogFragment";
    private static final String PARAM = "type";
    private static final String PARAM2 = "data";
    public static final String TYPE_TODO = "todo";
    public static final String TYPE_FOLDER = "folder";
    private String mType;
    private String mData;

    public DeleteDialogFragment() {
    }

    /**
     * @param param type
     * @param param2 data
     */
    public static DeleteDialogFragment newInstance(String param, String param2) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
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
        View v = inflater.inflate(R.layout.dialogfragment_delete, container);
        mType = getArguments().getString(PARAM);
        mData = getArguments().getString(PARAM2);
        FontContract font = new FontContract(getDialog().getContext().getAssets());

        TextView dialog_delete_title = (TextView)v.findViewById(R.id.dialogfragment_delete_title);
        TextView dialog_delete_content = (TextView)v.findViewById(R.id.dialogfragment_delete_content);
        Button dialog_delete_cancel_btn = (Button)v.findViewById(R.id.dialogfragment_delete_cancel_btn);
        Button dialog_delete_delete_btn = (Button)v.findViewById(R.id.dialogfragment_delete_delete_btn);

        dialog_delete_title.setTypeface(font.NahumSquareB_Regular());
        dialog_delete_content.setTypeface(font.NahumSquareR_Regular());
        dialog_delete_cancel_btn.setTypeface(font.NahumSquareR_Regular());
        dialog_delete_delete_btn.setTypeface(font.NahumSquareR_Regular());

        if(mType.equals(TYPE_FOLDER)){
            dialog_delete_title.setText("폴더 삭제");
            dialog_delete_content.setText("폴더 안의 ToDo도 전부 삭제됩니다.\n정말로 삭제하시겠습니까?");

            View.OnClickListener dialogClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.dialogfragment_delete_delete_btn){
                        SQLiteManager sqliteManager = SQLiteManager.getInstance(getDialog().getContext());
                        sqliteManager.deleteTodoFolder(mData);
                        dismiss();
                    } else {
                        dismiss();
                    }
                }
            };
            dialog_delete_cancel_btn.setOnClickListener(dialogClickListener);
            dialog_delete_delete_btn.setOnClickListener(dialogClickListener);
        }


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
