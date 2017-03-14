package com.team.codealmanac.w2do;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.models.User;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

    /* 구글 로그인 및 firebase 인증 순서
        (onCreate)GoogleSignInOptions 객체 생성 -> (onCreate)mGoogleApiClient 객체 생성 -> (onStart)AuthStateListener 등록 ->
        (f)signIn() -> (f)onActivityResult() -> (firebaseAuthWithGoogle)AuthCredential 호출 -> 호출 성공 시 AuthStateListener의 onAuthStateChanged 콜백
     */

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    private ImageView act_login_logo_image;
    //nonuser 레이아웃(로그인 안됐을 경우)
    private LinearLayout act_login_nonuser_layout;
    private Button act_login_signin_button;

    //user 레이아웃(로그인 됐을 경우)
    private LinearLayout act_login_user_layout;
    private ImageView act_login_user_image;
    private TextView act_login_username;
    private TextView act_login_email;
    private EditText act_login_nickname_edit;
    private Button act_login_nick_input_btn;

    private Animation animation;

    private DatabaseReference mUserReference;
    private DatabaseReference mPublicUserReference;
    private DatabaseReference mNicknameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mUserReference = FirebaseDatabase.getInstance().getReference().child("user");
        mPublicUserReference = FirebaseDatabase.getInstance().getReference().child("public_users");
        mNicknameReference = FirebaseDatabase.getInstance().getReference().child("nickname");

        //구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();

        Typeface font = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");

        act_login_logo_image = (ImageView)findViewById(R.id.act_login_logo_image);
        act_login_logo_image.setOnClickListener(this);
        //로그인 안되어있을 경우 레이아웃
        act_login_nonuser_layout = (LinearLayout)findViewById(R.id.act_login_nonuser_layout);
        TextView act_login_nonuser_greeting = (TextView)findViewById(R.id.act_login_nonuser_greeting);
        act_login_signin_button = (Button)findViewById(R.id.act_login_signin_button);
        act_login_signin_button.setOnClickListener(this);
        act_login_nonuser_greeting.setTypeface(font);

        // 로그인이 되어있을 경우 레이아웃
        act_login_user_layout = (LinearLayout)findViewById(R.id.act_login_user_layout);
        TextView act_login_user_greeting = (TextView)findViewById(R.id.act_login_user_greeting);
        act_login_user_image = (ImageView)findViewById(R.id.act_login_user_image);
        act_login_username = (TextView)findViewById(R.id.act_login_username);
        act_login_email = (TextView)findViewById(R.id.act_login_email);
        TextView act_login_nickname_msg = (TextView)findViewById(R.id.act_login_nickname_msg);
        act_login_nickname_edit = (EditText)findViewById(R.id.act_login_nickname_edit);
        act_login_nick_input_btn = (Button)findViewById(R.id.act_login_nick_input_btn);
        act_login_nick_input_btn.setOnClickListener(this);
        act_login_user_greeting.setTypeface(font);
        act_login_username.setTypeface(font);
        act_login_email.setTypeface(font);
        act_login_nickname_msg.setTypeface(font);
        act_login_nickname_edit.setTypeface(font);

        // Animation effects
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);

        //구글 로그인 세션 리스너
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(PreferencesManager.getNickname(LoginActivity.this.getApplicationContext()) != null){
                        //nickname을 이미 입력했으면
                        Log.d(TAG, "닉네임 있음");
                        startActivity( new Intent(LoginActivity.this,MainActivity.class) ); finish();
                    } else {
                        //nickname이 없으면 폰 데이터를 지웠을 경우 서버DB에는 닉네임 데이터가 있는지 확인해봐야함.
                        mNicknameReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {        //닉네임 데이터 한번 가져오기
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    PreferencesManager.setNickname(LoginActivity.this.getApplicationContext(), dataSnapshot.getValue().toString());
                                    startActivity( new Intent(LoginActivity.this,MainActivity.class) ); finish();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
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
        Log.d(TAG, "onActivityResult");
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

    // 로그인시 불리는 함수 (signin을 할 경우)
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential - onComplete: 로그인 성공");
                            //로그인 성공시 서버디비에 유저 정보 저장.
                            final FirebaseUser user = task.getResult().getUser();
                            User userModel = new User(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
                            mUserReference.child(user.getUid()).setValue(userModel);
                            mPublicUserReference.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) return;
                                    User publicUserModel = new User(user.getEmail(), user.getDisplayName());
                                    mPublicUserReference.child(mPublicUserReference.push().getKey()).setValue(publicUserModel);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        } else {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Log.d(TAG, "signIn");
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
        //프리퍼런스로 등록된 닉네임 데이터 삭제
        PreferencesManager.deleteNickname(getApplicationContext());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //로그인 됐을 경우 화면
            act_login_user_layout.setVisibility(View.VISIBLE);
            act_login_nonuser_layout.setVisibility(View.GONE);

            act_login_email.setText(user.getEmail());    //email정보
            act_login_username.setText(user.getDisplayName());   // 이름 정보

            // 사용자 cover photo -> glide 라이브러리 적용
            if(user.getPhotoUrl() != null ) {
                String personPhotoUrl = user.getPhotoUrl().toString();
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(act_login_user_image);
            }
        } else {
            //로그인 전 최초 화면
            act_login_nonuser_layout.setVisibility(View.VISIBLE);
            act_login_user_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "연결에 실패하였습니다. 데이터 네트워크를 킨 후 다시 시도해주십시오.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_login_signin_button:           //로그인 버튼 클릭
                signIn();
                break;
            case R.id.act_login_logo_image:               //로고 이미지 클릭
                signOut();
                break;
            case R.id.act_login_nick_input_btn:           //닉네임 입력 버튼 클릭
                String nickname = act_login_nickname_edit.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                    act_login_nickname_edit.setError("Required");
                    return;
                }
                mNicknameReference.child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue(nickname);
                PreferencesManager.setNickname(getApplicationContext(), nickname);

                startActivity( new Intent(LoginActivity.this,MainActivity.class) ); finish();
                break;
        }
    }
}
