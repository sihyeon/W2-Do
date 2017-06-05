package com.team.codealmanac.w2do.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.team.codealmanac.w2do.LoginActivity;
import com.team.codealmanac.w2do.R;

/**
 * Created by Choi Jaeung on 2017-06-05.
 */

public class GoogleAPIAssistant implements GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "GoogleAPIAssistant";
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private AppCompatActivity mActivity;
//    private static GoogleAPIAssistant sInstance;

//    public static GoogleAPIAssistant newInstance(Context context, AppCompatActivity activity){
//        if(sInstance == null){
//            sInstance = new GoogleAPIAssistant(context, activity);
//        }
//        return sInstance;
//    }

    public GoogleAPIAssistant(Context context, AppCompatActivity activity){
        this.mContext = context.getApplicationContext();
        this.mActivity = activity;
        configureSignIn();
        buildGoogleApiClient();
    }

    private void configureSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    private void buildGoogleApiClient() {
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public Intent signIn(){
//        mGoogleApiClient.clearDefaultAccountAndReconnect();
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void signOut(){
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(mGoogleApiClient.isConnected()){
                    Log.d(TAG, "logout");
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    Log.d(TAG, "user logout: " + status);
                                    mActivity.finishAffinity();
                                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                }
                            });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(mContext, "연결에 실패하였습니다. 데이터 네트워크를 킨 후 다시 시도해주십시오.", Toast.LENGTH_SHORT).show();
    }
}
