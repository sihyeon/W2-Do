package com.team.codealmanac.w2do.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;


/**
 * Created by sihyeon on 2017-01-15.
 */

public class SimpleInputDialog extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "SimpleInputDialog";
    private TextView act_simpleinput_header_text;
    private EditText act_simpleinput_edittext;
    private Button act_simpleinput_cancel_btn;
    private Button act_simpleinput_submit_btn;
    private FontContract mFontContract;

    private TodoFolder mFolder;
    private DatabaseReference mFolderReference;
    private DatabaseReference mSimpleTodoReference;
    private DatabaseReference mTodoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simpleinput);

        mFolderReference = FirebaseDatabase.getInstance().getReference().child("todo_folder").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mSimpleTodoReference = FirebaseDatabase.getInstance().getReference().child("simple_todo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mTodoReference = FirebaseDatabase.getInstance().getReference().child("todo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mFontContract = new FontContract(getApplication().getAssets());

        act_simpleinput_header_text = (TextView) findViewById(R.id.act_simpleinput_header_text);
        act_simpleinput_edittext = (EditText) findViewById(R.id.act_simpleinput_edittext);
        act_simpleinput_cancel_btn = (Button) findViewById(R.id.act_simpleinput_cancel_btn);
        act_simpleinput_submit_btn = (Button) findViewById(R.id.act_simpleinput_submit_btn);

        act_simpleinput_edittext.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_header_text.setTypeface(mFontContract.FranklinGothic_MediumCond());
        act_simpleinput_cancel_btn.setTypeface(mFontContract.NahumSquareR_Regular());
        act_simpleinput_submit_btn.setTypeface(mFontContract.NahumSquareR_Regular());

        act_simpleinput_cancel_btn.setOnClickListener(this);
        act_simpleinput_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.act_simpleinput_submit_btn) {
            if (TextUtils.isEmpty(act_simpleinput_edittext.getText())) {
                act_simpleinput_edittext.setError("내용을 입력해주세요.");
                return;
            }
            mFolderReference.child("folder").orderByValue().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot tempSnapshot = null;
                    mFolder = new TodoFolder(0, "auto-create", 1);
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        if (item.exists()) {
                            mFolder = item.getValue(TodoFolder.class);
                        }
                        tempSnapshot = item;
                        break;
                    }

                    SimpleTodo simpleTodo = new SimpleTodo(System.currentTimeMillis(),
                            act_simpleinput_edittext.getText().toString(), true, false);
                    Todo todo = new Todo(mFolder.todo_count, System.currentTimeMillis(), act_simpleinput_edittext.getText().toString(), mFolder.name);
                    String todoKey = mTodoReference.push().getKey();
                    mSimpleTodoReference.child(todoKey).setValue(simpleTodo);
                    mTodoReference.child(todoKey).setValue(todo);
                    if (!dataSnapshot.exists()) {
                        String key = mFolderReference.child("folder").push().getKey();
                        mFolderReference.child("folder").child(key).setValue(mFolder);
                        mFolderReference.child("folder_count").setValue(0);
                    } else {
                        tempSnapshot.getRef().child("todo_count").setValue(mFolder.todo_count + 1);
                    }
                    SimpleInputDialog.this.finish();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    SimpleInputDialog.this.finish();
                }
            });
        } else {
            finish();
        }
    }
}
