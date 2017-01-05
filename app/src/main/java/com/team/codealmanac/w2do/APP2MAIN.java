package com.team.codealmanac.w2do;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import com.team.codealmanac.w2do.adapter.AddRemoveNumberedAdapter;

public class APP2MAIN extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app2_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.app2_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setBackgroundColor(Color.parseColor("#30000000"));

        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4));
    }
}
