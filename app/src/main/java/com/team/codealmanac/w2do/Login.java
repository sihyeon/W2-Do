package com.team.codealmanac.w2do;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GOOGLE_LOGIN";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mStatusEmailView;
    private TextView mStatusTitleView;  //로그인 시 타이틀 뷰 변경되는 변수
    private ImageView mStatusImageView;

    private EditText nickname_edit;
    private Button input_go;
    private TextView nick_greeting_msg;

    private ProgressDialog mProgressDialog;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Header Views
        mStatusTextView = (TextView) findViewById(R.id.status_username);
        mStatusEmailView = (TextView) findViewById(R.id.status_email);
        mStatusTitleView = (TextView) findViewById(R.id.title_text);
        mStatusImageView = (ImageView)findViewById(R.id.user_image);
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
//      findViewById(R.id.disconnect_button).setOnClickListener(this);

        // Nickname View
        nick_greeting_msg = (TextView) findViewById(R.id.nickname_msg);
        input_go = (Button) findViewById(R.id.nick_input_btn);
        input_go.setOnClickListener(this);  // 버튼 클릭 이벤트
        nickname_edit = (EditText) findViewById(R.id.nickname_edit);

        // Animation effects
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);

        // [START configure_signin]
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // 로그인 성공 시 화면에 계정 이름 업데이트 함수
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusEmailView.setText(acct.getDisplayName());    // 이름 정보
            mStatusTextView.setText(acct.getEmail());   //email정보

            // 사용자 cover photo -> glide 라이브러리 적용
            if(acct.getPhotoUrl() != null ) {
                String personPhotoUrl = acct.getPhotoUrl().toString();
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mStatusImageView);
            } else {
                Glide.with(getApplicationContext()).load(R.drawable.btn_wtd)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(mStatusImageView);
            }
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    //[START GO 버튼 클릭 이벤트 함수]
    private void goClick(){
        Intent app2intent = new Intent(Login.this, APP2MAIN.class);
        startActivity(app2intent);
        finish();
    }


    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    //로그인 시 하단에 로그아웃, 연결해제 버튼 생성 함수
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            //로그인 시 변환되는 뷰
            mStatusTitleView.setText(R.string.signed_greeting_msg);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);

            //로그인 시 보여지는 뷰 선언
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            mStatusTextView.setVisibility(View.VISIBLE);
            mStatusEmailView.setVisibility(View.VISIBLE);
            mStatusImageView.setVisibility(View.VISIBLE);

            nick_greeting_msg.setVisibility(View.VISIBLE);
            input_go.setVisibility(View.VISIBLE);
            nickname_edit.setVisibility(View.VISIBLE);

            //로그인 시 보여지는 뷰에 적용하는 애니메이션 효과
            mStatusEmailView.startAnimation(animation);
            mStatusTitleView.startAnimation(animation);
            mStatusTextView.startAnimation(animation);
            nick_greeting_msg.startAnimation(animation);
            input_go.startAnimation(animation);
            nickname_edit.startAnimation(animation);


        } else {
            //로그아웃 시
            mStatusTextView.setVisibility(View.GONE);
            mStatusEmailView.setVisibility(View.GONE);
            mStatusTitleView.setText(R.string.signed_bye_msg);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

            findViewById(R.id.nickname_msg).setVisibility(View.GONE);
            findViewById(R.id.nick_input_btn).setVisibility(View.GONE);
            findViewById(R.id.nickname_edit).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            mStatusTitleView.startAnimation(animation);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.nick_input_btn:
                goClick();
                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
        }
    }
}