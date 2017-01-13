package com.team.codealmanac.w2do;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Choi Jaeung on 2017-01-12.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    protected static boolean isFirebaseDatabaseInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //파베 데이터베이스 오프라인 데이터 저장 설정
        if(!isFirebaseDatabaseInitialized){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isFirebaseDatabaseInitialized = true;
        }
    }

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
