package com.team.codealmanac.w2do.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SimpleInputDialogFragment extends DialogFragment implements View.OnClickListener {
    private final String TAG = "SimpleInputDialog";
    private EditText act_simpleinput_edittext;

    public SimpleInputDialogFragment() {}
    public static SimpleInputDialogFragment newInstance() {
        return new SimpleInputDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_simpleinput_todo_dialog, container);
        FontContract mFontContract = new FontContract(getDialog().getContext().getAssets());

        TextView act_simpleinput_header_text = (TextView) v.findViewById(R.id.act_simpleinput_header_text);
        act_simpleinput_edittext = (EditText) v.findViewById(R.id.act_simpleinput_edittext);
        Button act_simpleinput_cancel_btn = (Button) v.findViewById(R.id.act_simpleinput_cancel_btn);
        Button act_simpleinput_submit_btn = (Button) v.findViewById(R.id.act_simpleinput_submit_btn);

        act_simpleinput_edittext.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_header_text.setTypeface(mFontContract.FranklinGothic_MediumCond());
        act_simpleinput_cancel_btn.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_submit_btn.setTypeface(mFontContract.NahumSquareR_Regular());

        act_simpleinput_cancel_btn.setOnClickListener(this);
        act_simpleinput_submit_btn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.act_simpleinput_submit_btn) {
            if (TextUtils.isEmpty(act_simpleinput_edittext.getText())) {
                act_simpleinput_edittext.setError("내용을 입력해주세요.");
                return;
            }
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            long todayTimeInMillis = today.getTimeInMillis();
            Todo todo = new Todo(todayTimeInMillis, act_simpleinput_edittext.getText().toString());
            SQLiteManager sqliteManager = SQLiteManager.getInstance(getDialog().getContext());
            sqliteManager.addTodo(todo);
            dismiss();
        }else {
            dismiss();
        }
    }
}
