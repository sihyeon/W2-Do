package com.team.codealmanac.w2do;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Header Views
        mStatusTextView = (TextView) findViewById(R.id.status_username);
        mStatusEmailView = (TextView) findViewById(R.id.status_email);
        mStatusTitleView = (TextView) findViewById(R.id.title_text);
        mStatusImageView = (ImageView)findViewById(R.id.user_image);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        // Animation effects
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);


        // Nickname View
        nick_greeting_msg = (TextView) findViewById(R.id.nickname_msg);
        input_go = (Button) findViewById(R.id.nick_input_btn);
        input_go.setOnClickListener(this);  // 버튼 클릭 이벤트
        nickname_edit = (EditText) findViewById(R.id.nickname_edit);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusEmailView.setText(user.getDisplayName());    // 이름 정보
            mStatusTextView.setText(user.getEmail());   //email정보

            // 사용자 cover photo -> glide 라이브러리 적용
            if(user.getPhotoUrl() != null ) {
                String personPhotoUrl = user.getPhotoUrl().toString();
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mStatusImageView);
            } else {
                Glide.with(getApplicationContext()).load(R.drawable.btn_wtd)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(mStatusImageView);
            }

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
            mStatusImageView.setVisibility(View.INVISIBLE);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

            findViewById(R.id.nickname_msg).setVisibility(View.GONE);
            findViewById(R.id.nick_input_btn).setVisibility(View.GONE);
            findViewById(R.id.nickname_edit).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            mStatusTitleView.startAnimation(animation);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.nick_input_btn){
            Intent app2intent = new Intent(LoginActivity.this,APP2MAIN.class);
            startActivity(app2intent);
        }
    }

}
