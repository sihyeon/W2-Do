package com.team.codealmanac.w2do.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.team.codealmanac.w2do.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sihyeon on 2017-05-30.
 */

public class OpensourceDialogActivity extends Activity implements View.OnClickListener {
    private Button closeBtn;
    private ListView OpensourceList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogactivity_opensource);

        closeBtn = (Button)findViewById(R.id.close_btn);
        OpensourceList = (ListView)findViewById(R.id.opensource_listView);

        closeBtn.setOnClickListener(this);

        ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String, String>>();
        SimpleAdapter sap = new SimpleAdapter(this,mList,android.R.layout.simple_list_item_2,new String[]{"item1","item2"},
                new int[]{android.R.id.text1,android.R.id.text2});
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("item1","Android-Floating-Button");
        map.put("item2","https://github.com/futuresimple/android-floating-action-button");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","Android-GPUimage");
        map.put("item2","https://github.com/CyberAgent/android-gpuimage");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","Android-FirebaseUI");
        map.put("item2","https://github.com/firebase/FirebaseUI-Android");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","Color-Picker");
        map.put("item2","https://github.com/kristiyanP/colorpicker");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","Glide");
        map.put("item2","https://github.com/bumptech/glide");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","Giled-Transformation");
        map.put("item2","https://github.com/wasabeef/glide-transformations");
        mList.add(map);

        map = new HashMap<String, String>();
        map.put("item1","RelativeRadioGroup");
        map.put("item2","https://github.com/valheru7/RelativeRadioGroup");
        mList.add(map);

        OpensourceList.setAdapter(sap);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.close_btn){
            finish();
        }
    }
}
