package com.team.codealmanac.w2do;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = PermissionActivity.class.getSimpleName();
    private final int PERMISSIONS_REQUEST = 100;
    private String[] PERMISSIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        //위치정보, 미디어/파일 접근
        PERMISSIONS = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (hasPermissions()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        Button act_permission_cancel_btn = (Button) findViewById(R.id.act_permission_cancel_btn);
        Button act_permission_ok_btn = (Button) findViewById(R.id.act_permission_ok_btn);

        act_permission_cancel_btn.setOnClickListener(this);
        act_permission_ok_btn.setOnClickListener(this);
    }

    private boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                //권한 없음
                return false;
            }
        }
        //권한 체크 통과
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.act_permission_ok_btn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST);
        } else if (v.getId() == R.id.act_permission_cancel_btn) {
            finishAffinity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "권한을 거부하면 원할한 이용이 어려울 수 있습니다. 설정 - 어플리케이션 관리 - W2do - 권한에서 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }
    }
}


